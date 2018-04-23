package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.BuildConfig;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.api.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackVoteBean;
import ch.uzh.supersede.feedbacklibrary.beans.SubscriptionRequestBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.AbstractSettingsListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.SubscriptionsListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.VotesListItem;
import ch.uzh.supersede.feedbacklibrary.configurations.ConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import ch.uzh.supersede.feedbacklibrary.beans.ConfigurationRequestBean;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType;
import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType.*;
import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType.CREATE_SUBSCRIPTION;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ServicesConstants.FEEDBACK_SERVICE_TAG;

/**
 * Singleton class that returns the original {@link FeedbackApiService} with its functions, defined in {@link IFeedbackAPI} iff {@code BuildConfig.DEBUG} is enabled, otherwise
 * {@link FeedbackMockService} will be used instead. Classes that use this Service will have to implement {@link IFeedbackServiceEventListener} and its functions in order to handle the
 * asynchronous events accordingly.
 */
public abstract class FeedbackService {
    private static FeedbackService instance;
    private static IFeedbackAPI feedbackAPI;

    private FeedbackService() {
    }

    public static FeedbackService getInstance() {
        if (BuildConfig.DEBUG) {
            if (instance == null) {
                instance = new FeedbackMockService();
            }
            return instance;
        }
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EXTRA_KEY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            feedbackAPI = retrofit.create(IFeedbackAPI.class);
            instance = new FeedbackApiService();
        }
        return instance;
    }

    public abstract void pingOrchestrator(IFeedbackServiceEventListener callback);

    public abstract void pingRepository(IFeedbackServiceEventListener callback);

    public abstract void createFeedbackVariant(IFeedbackServiceEventListener callback, String language, long applicationId, MultipartBody.Part feedback, List<MultipartBody.Part> files);

    public abstract void getConfiguration(IFeedbackServiceEventListener callback, ConfigurationRequestBean configurationRequestBean);

    public abstract void execImportConfigurationAndStartActivity(ConfigurationRequestBean configurationRequestBean, Response<OrchestratorConfigurationItem> response);

    public abstract void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void createSubscription(IFeedbackServiceEventListener callback, SubscriptionRequestBean subscriptionRequestBean);

    private static class FeedbackApiService extends FeedbackService {
        @Override
        public void pingOrchestrator(IFeedbackServiceEventListener callback) {
            feedbackAPI.pingOrchestrator().enqueue(
                    new FeedbackCallback<ResponseBody>(callback, EventType.PING_ORCHESTRATOR) {
                    });
        }

        @Override
        public void pingRepository(IFeedbackServiceEventListener callback) {
            feedbackAPI.pingRepository().enqueue(
                    new FeedbackCallback<ResponseBody>(callback, EventType.PING_REPOSITORY) {
                    });
        }

        @Override
        public void createFeedbackVariant(IFeedbackServiceEventListener callback, String language, long applicationId, MultipartBody.Part feedback, List<MultipartBody.Part> files) {
            feedbackAPI.createFeedbackVariant(language, applicationId, feedback, files).enqueue(
                    new FeedbackCallback<JsonObject>(callback, EventType.CREATE_FEEDBACK_VARIANT) {
                    });
        }

        @Override
        public void getConfiguration(IFeedbackServiceEventListener callback, ConfigurationRequestBean configurationRequestBean) {
            feedbackAPI.getConfiguration(configurationRequestBean.getLanguage(), configurationRequestBean.getApplicationId()).enqueue(
                    new FeedbackCallback<OrchestratorConfigurationItem>(callback, EventType.GET_CONFIGURATION) {
                    });
        }

        @Override
        public void execImportConfigurationAndStartActivity(ConfigurationRequestBean configurationRequestBean, Response<OrchestratorConfigurationItem> response) {
            if (response == null || response.body() == null) {
                return;
            }

            OrchestratorConfigurationItem configuration = response.body();
            List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
            long selectedPullConfigurationIndex = -1;
            ConfigurationItem selectedConfigurationItem = null;
            Log.d(FEEDBACK_SERVICE_TAG, " Application with ID [" + configurationRequestBean.getApplicationId() + "] has " + configurationItems.size() + "  configurations!");
            for (ConfigurationItem configurationItem : configurationItems) {
                if (configurationItem
                        .getType()
                        .equals("PULL") && configurationItem.getId() == configurationRequestBean
                        .getPullConfigurationId()) {
                    selectedPullConfigurationIndex = configurationItem.getId();
                    selectedConfigurationItem = configurationItem;
                    break;
                }
            }

            if (selectedPullConfigurationIndex != -1 && selectedConfigurationItem != null) {
                // If no "showIntermediateDialog" is provided, show it
                boolean showIntermediateDialog = true;
                for (Map<String, Object> parameter : selectedConfigurationItem.getGeneralConfigurationItem().getParameters()) {
                    String key = (String) parameter.get("key");
                    showIntermediateDialog = isShowIntermediateDialog(showIntermediateDialog, parameter, key);
                }

                String jsonConfiguration = new GsonBuilder()
                        .setLenient()
                        .create()
                        .toJson(configuration);
                //Notabene, hier wird die ganze Konfiguration verwendet, nicht Bruchstuecke..

                Intent intent = createFeedbackIntentFromPull(configurationRequestBean, jsonConfiguration, selectedPullConfigurationIndex);

                if (!showIntermediateDialog) {
                    // Start the feedback activity without asking the user
                    configurationRequestBean
                            .getStartingActivity()
                            .startActivity(intent);
                } else {
                    // Ask the user if he would like to give feedback or not
                    DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(configurationRequestBean.getIntermediateDialogText(), jsonConfiguration,
                            selectedPullConfigurationIndex, configurationRequestBean.getUrl(), configurationRequestBean.getLanguage());
                    d.show(configurationRequestBean.getStartingActivity().getFragmentManager(), "feedbackPopupDialog");
                }
            } else {
                DialogUtils.showInformationDialog(configurationRequestBean.getStartingActivity(), new String[]{configurationRequestBean
                        .getStartingActivity()
                        .getResources().getString(R.string.info_application_unavailable)}, true);
            }
        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            //TODO [jfo] implement
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            //TODO [jfo] implement
        }

        @Override
        public void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity) {
            //TODO [jfo] implement
        }

        @Override
        public void createSubscription(IFeedbackServiceEventListener callback, SubscriptionRequestBean subscriptionRequestBean) {
            //TODO [jfo] implement
        }

        private boolean isShowIntermediateDialog(boolean showIntermediateDialog, Map<String, Object> parameter, String key) {
            if (key.equals("showIntermediateDialog")) {
                showIntermediateDialog = Utils.intToBool(Integer.valueOf(parameter.get("value").toString()));
            }
            return showIntermediateDialog;
        }

        /**
         * Starts an activity with the orchestrator-configuration
         */
        private static Intent createFeedbackIntentFromPull(ConfigurationRequestBean configWrapper, String jsonConfiguration, long selectedPullConfigurationIndex) {
            Intent intent = new Intent(configWrapper.getStartingActivity(), configWrapper.getActivityToStart());
            intent.putExtra(IS_PUSH_STRING, false);
            intent.putExtra(JSON_CONFIGURATION_STRING, jsonConfiguration);
            intent.putExtra(SELECTED_PULL_CONFIGURATION_INDEX, selectedPullConfigurationIndex);
            intent.putExtra(EXTRA_KEY_BASE_URL, configWrapper.getUrl());
            intent.putExtra(EXTRA_KEY_LANGUAGE, configWrapper.getLanguage());
            return intent;
        }
    }

    private static class FeedbackMockService extends FeedbackService {
        @Override
        public void pingOrchestrator(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(PING_ORCHESTRATOR, null);
        }

        @Override
        public void pingRepository(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(PING_REPOSITORY, null);
        }

        @Override
        public void createFeedbackVariant(IFeedbackServiceEventListener callback, String language, long applicationId, MultipartBody.Part feedback, List<MultipartBody.Part> files) {
            callback.onEventCompleted(CREATE_FEEDBACK_VARIANT, null);
        }

        @Override
        public void getConfiguration(IFeedbackServiceEventListener callback, ConfigurationRequestBean configurationRequestBean) {
            callback.onEventCompleted(GET_CONFIGURATION, null);
        }

        @Override
        public void execImportConfigurationAndStartActivity(ConfigurationRequestBean configurationRequestBean, Response<OrchestratorConfigurationItem> response) {
            //TODO [jfo] probably not needed
        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            ArrayList<AbstractSettingsListItem> feedbackList = new ArrayList<>();
            for (FeedbackBean feedback : RepositoryStub.generateFeedback(activity, 25, 0, 50, 0.0f)) {
                for (FeedbackVoteBean bean : feedback.getVotes()) {
                    feedbackList.add(new VotesListItem(activity, 8, bean));
                }
            }
            callback.onEventCompleted(GET_MINE_FEEDBACK_VOTES, feedbackList);
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            ArrayList<AbstractSettingsListItem> feedbackList = new ArrayList<>();

            for (FeedbackBean feedback : RepositoryStub.generateFeedback(activity, 25, 0, 50, 1.0f)) {
                for (FeedbackVoteBean bean : feedback.getVotes()) {
                    feedbackList.add(new VotesListItem(activity, 8, bean));
                }
            }

            callback.onEventCompleted(GET_OTHERS_FEEDBACK_VOTES, feedbackList);
        }

        @Override
        public void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity) {
            ArrayList<AbstractSettingsListItem> feedbackList = new ArrayList<>();
            for (FeedbackBean bean : RepositoryStub.generateFeedback(activity, 25, 0, 50, 0.25f)) {
                feedbackList.add(new SubscriptionsListItem(activity, 8, bean));
            }
            callback.onEventCompleted(GET_FEEDBACK_SETTINGS, feedbackList);
        }

        @Override
        public void createSubscription(IFeedbackServiceEventListener callback, SubscriptionRequestBean subscriptionRequestBean) {
            callback.onEventCompleted(CREATE_SUBSCRIPTION, subscriptionRequestBean);
        }
    }
}
