package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackVoteBean;

public class FeedbackUtility {
    private FeedbackUtility() {
        // nop
    }

    public static int getMinUpVotes(List<FeedbackBean> feedbackBeans) {
        return getUpVotes(feedbackBeans, false);
    }

    public static int getMaxUpVotes(List<FeedbackBean> feedbackBeans) {
        return getUpVotes(feedbackBeans, true);
    }

    public static int getUpVotes(List<FeedbackVoteBean> votes) {
        int voteCount = 0;
        for (FeedbackVoteBean vote : votes) {
            voteCount += vote.getVote();
        }
        return voteCount;
    }

    public static int getUpVotes(FeedbackBean feedback) {
        return getUpVotes(feedback.getVotes());
    }

    public static String getUpVotesAsText(FeedbackBean feedback) {
        int voteCount = getUpVotes(feedback);
        String votes = Integer.toString(voteCount);
        return voteCount > 0 ? "+" + votes : (voteCount < 0 ? "-" + votes : votes);
    }

    private static int getUpVotes(List<FeedbackBean> feedbackBeans, boolean isMax) {
        int maxVotes = 0;
        int minVotes = 0;

        for (FeedbackBean feedback : feedbackBeans) {
            int voteCount = getUpVotes(feedback);
            if (voteCount > maxVotes) {
                maxVotes = voteCount;
            } else if (voteCount < minVotes) {
                minVotes = voteCount;
            }
        }

        return isMax ? maxVotes : minVotes;
    }
}
