package ch.uzh.supersede.feedbacklibrary.beans;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

public class FeedbackBean implements Serializable {

    @SuppressWarnings("squid:UnusedPrivateMethod")
    public enum FEEDBACK_STATUS {
        OPEN("Open", Color.rgb(0, 150, 255)),
        IN_PROGRESS("In Progress", Color.rgb(222, 222, 0)),
        REJECTED("Rejected", Color.rgb(255, 150, 0)),
        DUPLICATE("Duplicate", Color.rgb(150, 150, 150)),
        CLOSED("Closed", Color.rgb(0, 222, 100));

        FEEDBACK_STATUS(String label, int color) {
            this.label = label;
            this.color = color;
        }

        private int color;
        private String label;

        public int getColor() {
            return color;
        }

        public String getLabel() {
            return label;
        }
    }

    private String id;
    private String title;
    private String userName;
    private String technicalUserName;
    private long timeStamp;
    private List<FeedbackVoteBean> votes;
    private int upVotes;
    private int responses;
    private FEEDBACK_STATUS feedbackStatus;
    private boolean isSubscribed;

    private FeedbackBean() {
    }

    public static class Builder {
        private String id;
        private String title;
        private String userName;
        private String technicalUserName;
        private long timeStamp;
        private List<FeedbackVoteBean> votes;
        private int upVotes;
        private int responses;
        private FEEDBACK_STATUS feedbackStatus;
        private boolean isSubscribed;

        public Builder() {
            //NOP
        }

        public Builder(String id) {
            this.id = id;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withTechnicalUserName(String technicalUserName) {
            this.technicalUserName = technicalUserName;
            return this;
        }

        public Builder withTimestamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder withVotes(List<FeedbackVoteBean> votes) {
            this.votes = new ArrayList<>(votes);
            return this;
        }

        public Builder withUpVotes(int upVotes) {
            this.upVotes = upVotes;
            return this;
        }

        public Builder withResponses(int responses) {
            this.responses = responses;
            return this;
        }

        public Builder withStatus(FEEDBACK_STATUS feedbackStatus) {
            this.feedbackStatus = feedbackStatus;
            return this;
        }

        public Builder withIsSubscribed(boolean isSubscribed) {
            this.isSubscribed = isSubscribed;
            return this;
        }

        public FeedbackBean build() {
            if (CompareUtility.notNull(id, title, userName, technicalUserName, timeStamp, votes, upVotes, feedbackStatus, isSubscribed)) {
                FeedbackBean bean = new FeedbackBean();
                bean.id = this.id;
                bean.title = this.title;
                bean.userName = this.userName;
                bean.technicalUserName = this.technicalUserName;
                bean.timeStamp = this.timeStamp;
                bean.votes = this.votes;
                bean.upVotes = this.upVotes;
                bean.responses = this.responses;
                bean.feedbackStatus = this.feedbackStatus;
                bean.isSubscribed = this.isSubscribed;
                return bean;
            }
            return null;
        }
    }

    public String getId() {
        return this.id;
    }

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

    public int getResponses() {
        return responses;
    }

    public FEEDBACK_STATUS getFeedbackStatus() {
        return feedbackStatus;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public List<FeedbackVoteBean> getVotes() {
        return votes;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

}
