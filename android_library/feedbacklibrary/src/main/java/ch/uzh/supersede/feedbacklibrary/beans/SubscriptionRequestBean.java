package ch.uzh.supersede.feedbacklibrary.beans;

import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

public class SubscriptionRequestBean {
    private String userId;
    private String technicalUserName;
    private String feedbackId;
    private boolean isSubscribe;

    public String getUserId() {
        return userId;
    }

    public String getTechnicalUserName() {
        return technicalUserName;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public boolean getSubscribe() {
        return isSubscribe;
    }

    private SubscriptionRequestBean() {
    }

    public static class Builder {
        private String userId;
        private String technicalUserName;
        private String feedbackId;
        private boolean isSubscribe;

        public Builder() {
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withTechnicalUserName(String technicalUserName) {
            this.technicalUserName = technicalUserName;
            return this;
        }

        public Builder withFeedbackId(String feedbackId) {
            this.feedbackId = feedbackId;
            return this;
        }

        public Builder withIsSubscribe(boolean isSubscribe) {
            this.isSubscribe = isSubscribe;
            return this;
        }

        public SubscriptionRequestBean build() {
            if (CompareUtility.notNull(userId, technicalUserName, feedbackId, isSubscribe)) {
                SubscriptionRequestBean bean = new SubscriptionRequestBean();
                bean.userId = this.userId;
                bean.technicalUserName = this.technicalUserName;
                bean.feedbackId = this.feedbackId;
                bean.isSubscribe = this.isSubscribe;
                return bean;
            }
            return null;
        }
    }
}
