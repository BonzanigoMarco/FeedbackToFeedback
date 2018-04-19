package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.ConfigurationRequestBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackVoteBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.AbstractSettingsItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.VotesListItem;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import okhttp3.MultipartBody;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType.*;

public class FeedbackMockService {
    private static FeedbackMockService instance;

    private FeedbackMockService() {
    }

    public static FeedbackMockService getInstance() {
        if (instance == null) {
            instance = new FeedbackMockService();
        }
        return instance;
    }

    public void pingOrchestrator(IFeedbackMockServiceEventListener callback) {
        callback.onEventCompleted(PING_ORCHESTRATOR, null);
    }

    public void pingRepository(IFeedbackMockServiceEventListener callback) {
        callback.onEventCompleted(PING_REPOSITORY, null);
    }

    public void createFeedbackVariant(IFeedbackMockServiceEventListener callback, String language, long applicationId, MultipartBody.Part feedback, List<MultipartBody.Part> files) {
        callback.onEventCompleted(CREATE_FEEDBACK_VARIANT, null);
    }

    public void getConfiguration(IFeedbackMockServiceEventListener callback, ConfigurationRequestBean configurationRequestBean) {
        callback.onEventCompleted(GET_CONFIGURATION, null);
    }

    public void getMineFeedbackVotes(IFeedbackMockServiceEventListener callback, Activity activity){
        ArrayList<AbstractSettingsItem> feedbackList = new ArrayList<>();
        for (FeedbackVoteBean bean : RepositoryStub.generateFeedbackVote(activity, 25,  0.85f, 0.0f)) {
            feedbackList.add(new VotesListItem(activity, 8, bean));
        }
        callback.onEventCompleted(GET_MINE_FEEDBACK_VOTES, feedbackList);
    }

    public void getOthersFeedbackVotes(IFeedbackMockServiceEventListener callback, Activity activity){
        ArrayList<AbstractSettingsItem> feedbackList = new ArrayList<>();
        for (FeedbackVoteBean bean : RepositoryStub.generateFeedbackVote(activity, 25,  0.85f, 1.0f)) {
            feedbackList.add(new VotesListItem(activity, 8, bean));
        }
        callback.onEventCompleted(GET_OTHERS_FEEDBACK_VOTES, feedbackList);
    }

    public void getFeedbackSettings(IFeedbackMockServiceEventListener callback, Activity activity){
        ArrayList<AbstractSettingsItem> feedbackList = new ArrayList<>();
        for (FeedbackBean bean : RepositoryStub.generateFeedback(activity, 25, -5, 50, 0.25f)) {
            feedbackList.add(new VotesListItem(activity, 8, bean));
        }
        callback.onEventCompleted(GET_SETTINGS_FEEDBACK, feedbackList);
    }
}
