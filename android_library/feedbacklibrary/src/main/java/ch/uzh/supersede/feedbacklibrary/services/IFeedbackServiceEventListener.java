package ch.uzh.supersede.feedbacklibrary.services;

import retrofit2.Response;

public interface IFeedbackServiceEventListener {
    enum EventType {
        PING_ORCHESTRATOR,
        PING_REPOSITORY,
        CREATE_FEEDBACK_VARIANT,
        GET_CONFIGURATION,
        GET_MINE_FEEDBACK_VOTES,
        GET_OTHERS_FEEDBACK_VOTES,
        GET_SETTINGS_FEEDBACK
    }

    void onEventCompleted(EventType eventType, Response response);

    void onEventFailed(EventType eventType, Response response);

    void onConnectionFailed(EventType eventType);
}
