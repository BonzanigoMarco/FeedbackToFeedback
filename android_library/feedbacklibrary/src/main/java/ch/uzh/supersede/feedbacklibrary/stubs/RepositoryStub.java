package ch.uzh.supersede.feedbacklibrary.stubs;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackVoteBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean.FEEDBACK_STATUS;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.TECHNICAL_USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;
import static ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean.FEEDBACK_STATUS.*;

public class RepositoryStub {
    private RepositoryStub() {
    }

    public static List<FeedbackBean> generateFeedback(Context context, int count, int minUpVotes, int maxUpVotes, float ownFeedbackPercent) {
        ArrayList<FeedbackBean> feedbackBeans = new ArrayList<>();
        for (int f = 0; f < count; f++) {
            feedbackBeans.add(generateFeedback(context, minUpVotes, maxUpVotes, ownFeedbackPercent));
        }
        return feedbackBeans;
    }

    public static List<FeedbackVoteBean> generateFeedbackVote(Context context, int count,  float upVoteProbability, float ownFeedbackPercent){
        ArrayList<FeedbackVoteBean> feedbackVoteBeans = new ArrayList<>();
        for (int f = 0; f < count; f++) {
            feedbackVoteBeans.add(generateFeedbackVote(context, upVoteProbability, ownFeedbackPercent));
        }
        return feedbackVoteBeans;
    }

    private static FeedbackBean generateFeedback(Context context, int minUpVotes, int maxUpVotes, float ownFeedbackPercent) {
        int upperBound = ownFeedbackPercent > 0 ? NumberUtility.divide(1, ownFeedbackPercent) : 0;
        boolean ownFeedback = ACTIVE.check(context) && NumberUtility.randomInt(0, upperBound > 0 ? upperBound - 1 : upperBound) == 0;
        FEEDBACK_STATUS feedbackStatus = generateFeedbackStatus();
        String title = generateTitle();
        String userName = generateUserName(context, ownFeedback);
        String technicalUserName = generateTechnicalUserName(context, ownFeedback);
        long timeStamp = generateTimestamp();
        int upVotes = generateUpVotes(minUpVotes, maxUpVotes, feedbackStatus);
        int responses = generateResponses();

        return new FeedbackBean.Builder()
                .withTitle(title)
                .withUserName(userName)
                .withTechnicalUserName(technicalUserName)
                .withTimestamp(timeStamp)
                .withUpVotes(upVotes)
                .withMinUpVotes(minUpVotes)
                .withMaxUpVotes(maxUpVotes)
                .withResponses(responses)
                .withStatus(feedbackStatus)
                .build();
    }

    private static FeedbackVoteBean generateFeedbackVote(Context context, float upVoteProbability, float ownFeedbackPercent) {
        int upperBound = ownFeedbackPercent > 0 ? NumberUtility.divide(1, ownFeedbackPercent) : 0;
        boolean ownFeedback = ACTIVE.check(context) && NumberUtility.randomInt(0, upperBound > 0 ? upperBound - 1 : upperBound) == 0;
        String title = generateTitle();
        String userName = generateUserName(context, ownFeedback);
        String technicalUserName = generateTechnicalUserName(context, ownFeedback);
        long timeStamp = generateTimestamp();
        int vote = generateVote(upVoteProbability);

        return new FeedbackVoteBean.Builder()
                .withTitle(title)
                .withUserName(userName)
                .withTechnicalUserName(technicalUserName)
                .withTimestamp(timeStamp)
                .withVote(vote)
                .build();
    }

    private static FEEDBACK_STATUS generateFeedbackStatus() {
        FEEDBACK_STATUS[] status = new FEEDBACK_STATUS[]{OPEN, IN_PROGRESS, REJECTED, DUPLICATE, CLOSED};
        return status[NumberUtility.randomPosition(status)];
    }

    private static int generateVote(float upVoteProbability) {
        if (upVoteProbability <= 0) {
            return 0;
        } else if (upVoteProbability >= 1) {
            return 1;
        }
        return (int) Math.round((1.0 - Math.random()) * upVoteProbability);
    }

    private static int generateResponses() {
        return NumberUtility.randomInt(0, 50);
    }

    private static int generateUpVotes(int minUpVotes, int maxUpVotes, FEEDBACK_STATUS feedbackStatus) {
        if (CompareUtility.oneOf(feedbackStatus, REJECTED)) {
            return NumberUtility.randomInt(minUpVotes, -1);
        } else if (CompareUtility.oneOf(feedbackStatus, DUPLICATE)) {
            return 0;
        }
        return NumberUtility.randomInt(0, maxUpVotes);
    }

    private static long generateTimestamp() {
        return DateUtility.getPastDateLong(2);
    }

    @NonNull
    private static String generateTechnicalUserName(Context context, boolean own) {
        if (own) {
            return FeedbackDatabase.getInstance(context).readString(TECHNICAL_USER_NAME, null);
        }
        return UUID.randomUUID().toString();
    }

    @NonNull
    private static String generateUserName(Context context, boolean own) {
        if (own) {
            return FeedbackDatabase.getInstance(context).readString(USER_NAME, null);
        }
        return RepositoryStub.getUniqueName(GeneratorStub.BagOfNames.pickRandom());
    }

    @NonNull
    private static String generateTitle() {
        return GeneratorStub.BagOfLabels.pickRandom().concat("-Feedback");
    }

    //Should be generated on the Server
    //Return value is something like Jake --> Jake#12345678 (random 8 digits)
    public static String getUniqueName(String name) {
        return name.concat("#").concat(String.valueOf(NumberUtility.multiply(99999999, Math.random())));
    }

}
