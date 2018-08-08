package ch.uzh.supersede.feedbacklibrary.beans;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS;

public class FeedbackDetailsBean implements Serializable {

    private long feedbackId;
    private String description;
    private String userName;
    private long timeStamp;
    private int upVotes;
    private FEEDBACK_STATUS feedbackStatus;
    private List<FeedbackResponseBean> feedbackResponses = new ArrayList<>();
    private FeedbackBean feedbackBean;
    private byte[] bitmapBytes;
    private String bitmapName;
    private boolean isSubscribed;
    private boolean isPublic;

    private FeedbackDetailsBean() {
    }

    public static class Builder {
        private long feedbackId;
        private String description;
        private String userName;
        private long timeStamp;
        private int upVotes;
        private FEEDBACK_STATUS feedbackStatus;
        private List<FeedbackResponseBean> feedbackResponses = new ArrayList<>();
        private byte[] bitmap;
        private String bitmapName;
        private FeedbackBean feedbackBean;
        private boolean isSubscribed;
        private boolean isPublic;

        public Builder() {
            //NOP
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withTimestamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder withUpVotes(int upVotes) {
            this.upVotes = upVotes;
            return this;
        }

        public Builder withStatus(FEEDBACK_STATUS feedbackStatus) {
            this.feedbackStatus = feedbackStatus;
            return this;
        }

        public Builder withResponses(List<FeedbackResponseBean> feedbackResponses) {
            this.feedbackResponses = feedbackResponses;
            return this;
        }

        public Builder withFeedbackBean(FeedbackBean feedbackBean) {
            this.feedbackBean = feedbackBean;
            return this;
        }

        public Builder withFeedbackId(long feedbackId) {
            this.feedbackId = feedbackId;
            return this;
        }

        public Builder withBitmap(Bitmap bitmap) {
            this.bitmap = ImageUtility.imageToBytes(bitmap);
            return this;
        }

        public Builder withBitmapName(String bitmapName) {
            this.bitmapName = bitmapName;
            return this;
        }

        public Builder withSubscription(boolean isSubscribed){
            this.isSubscribed = isSubscribed;
            return this;
        }

        public Builder isPublic (boolean isPublic){
            this.isPublic = isPublic;
            return this;
        }

        public FeedbackDetailsBean build() {
            if (CompareUtility.notNull(feedbackId, timeStamp, feedbackStatus, feedbackBean)) {
                FeedbackDetailsBean bean = new FeedbackDetailsBean();
                bean.feedbackId = feedbackId;
                bean.description = this.description;
                bean.userName = this.userName;
                bean.timeStamp = this.timeStamp;
                bean.upVotes = this.upVotes;
                bean.feedbackStatus = this.feedbackStatus;
                bean.feedbackResponses = this.feedbackResponses;
                bean.feedbackBean = this.feedbackBean;
                bean.bitmapBytes = this.bitmap;
                bean.bitmapName = this.bitmapName;
                bean.isSubscribed = this.isSubscribed;
                bean.isPublic = this.isPublic;
                return bean;
            }
            return null;
        }
    }

    public String getTitle() {
        return feedbackBean.getTitle();
    }

    public String getDescription() {
        return description;
    }

    public String getUserName() {
        return userName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public String getUpVotesAsText() {
        return FeedbackUtility.getUpvotesAsText(upVotes);
    }

    public List<FeedbackResponseBean> getResponses() {
        return feedbackResponses;
    }

    public FEEDBACK_STATUS getFeedbackStatus() {
        return feedbackStatus;
    }

    public FeedbackBean getFeedbackBean() {
        return feedbackBean;
    }

    public String[] getTags() {
        return feedbackBean.getTags();
    }

    public long getFeedbackId() {
        return feedbackId;
    }

    public Bitmap getBitmap() {
        return ImageUtility.bytesToImage(bitmapBytes);
    }

    public String getBitmapName() {
        return bitmapName;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
