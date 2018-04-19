package ch.uzh.supersede.feedbacklibrary.services;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.ConfigurationRequestBean;
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

    public void getMineFeedbackVotes(IFeedbackMockServiceEventListener callback){
        callback.onEventCompleted(GET_MINE_FEEDBACK_VOTES, null);
    }

    public void getOthersFeedbackVotes(IFeedbackMockServiceEventListener callback){
        callback.onEventCompleted(GET_OTHERS_FEEDBACK_VOTES, null);
    }

    public void getFeedbackSettings(IFeedbackMockServiceEventListener callback){
        callback.onEventCompleted(GET_SETTINGS_FEEDBACK, null);
    }
}
