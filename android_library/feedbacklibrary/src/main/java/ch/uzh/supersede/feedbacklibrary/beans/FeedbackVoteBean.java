package ch.uzh.supersede.feedbacklibrary.beans;

import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

public class FeedbackVoteBean extends AbstractFeedbackBean {
    private int vote;

    private FeedbackVoteBean() {
    }

    public int getVote() {
        return vote;
    }

    public static class Builder extends AbstractFeedbackBean.Builder<Builder> {
        private int vote;

        public Builder() {
            //NOP
        }

        @Override
        Builder getThis() {
            return this;
        }

        public Builder withVote(int vote) {
            this.vote = vote;
            return this;
        }

        @Override
        public FeedbackVoteBean build() {
            if (CompareUtility.notNull(title, userName, technicalUserName, timeStamp, vote)) {
                FeedbackVoteBean bean = new FeedbackVoteBean();
                bean.title = this.title;
                bean.userName = this.userName;
                bean.technicalUserName = this.technicalUserName;
                bean.timeStamp = this.timeStamp;
                bean.vote = this.vote;
                return bean;
            }
            return null;
        }
    }
}
