package ch.uzh.supersede.feedbacklibrary.beans;

import java.io.Serializable;

public abstract class AbstractFeedbackBean implements Serializable {
    String title;
    String userName;
    String technicalUserName;
    long timeStamp;

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public String getTechnicalUserName() {
        return technicalUserName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public abstract static class Builder<T extends Builder<T>> {
        String title;
        String userName;
        String technicalUserName;
        long timeStamp;

        abstract T getThis();

        public T withTitle(String title) {
            this.title = title;
            return getThis();
        }

        public T withUserName(String userName) {
            this.userName = userName;
            return getThis();
        }

        public T withTechnicalUserName(String technicalUserName) {
            this.technicalUserName = technicalUserName;
            return getThis();
        }

        public T withTimestamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return getThis();
        }

        public abstract AbstractFeedbackBean build();
    }
}
