package ch.uzh.supersede.feedbacklibrary.services;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.*;

public interface IFeedbackMockServiceEventListener {

    void onEventCompleted(EventType eventType, Object response);

    void onEventFailed(EventType eventType, Object response);

    void onConnectionFailed(EventType eventType);
}
