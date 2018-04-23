package ch.uzh.supersede.feedbacklibrary.stubs;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean.FEEDBACK_STATUS;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackVoteBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.utils.DateUtility;
import ch.uzh.supersede.feedbacklibrary.utils.NumberUtility;

import static ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean.FEEDBACK_STATUS.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class RepositoryStub {
    private RepositoryStub() {
    }

    public static FeedbackBean getFeedback(Context context, String feedbackId) {
        int minVotes = 0;
        int maxVotes = 100;
        float ownFeedbackPercent = 0.1f;
        //FIXME [jfo] do not generate new feedback but get stored one
        return generateFeedback(context, minVotes, maxVotes, ownFeedbackPercent, feedbackId);
    }

    public static List<FeedbackBean> generateFeedback(Context context, int count, int minVotes, int maxVotes, float ownFeedbackPercent) {
        ArrayList<FeedbackBean> feedbackBeans = new ArrayList<>();
        for (int f = 0; f < count; f++) {
            feedbackBeans.add(generateFeedback(context, minVotes, maxVotes, ownFeedbackPercent));
        }
        return feedbackBeans;
    }

    private static List<FeedbackVoteBean> generateFeedbackVotes(Context context, int count, String feedbackId) {
        ArrayList<FeedbackVoteBean> feedbackVoteBeans = new ArrayList<>();
        for (int f = 0; f < count; f++) {
            feedbackVoteBeans.add(generateFeedbackVote(context, feedbackId));
        }
        return feedbackVoteBeans;
    }

    private static FeedbackBean generateFeedback(Context context, int minVotes, int maxVotes, float ownFeedbackPercent) {
        String feedbackId = UUID.randomUUID().toString();
        return generateFeedback(context, minVotes, maxVotes, ownFeedbackPercent, feedbackId);
    }

    private static FeedbackBean generateFeedback(Context context, int minVotes, int maxVotes, float ownFeedbackPercent, String feedbackId) {
        int upperBound = ownFeedbackPercent > 0 ? NumberUtility.divide(1, ownFeedbackPercent) : 0;
        boolean ownFeedback = ACTIVE.check(context) && NumberUtility.randomInt(0, upperBound > 0 ? upperBound - 1 : upperBound) == 0;
        FEEDBACK_STATUS feedbackStatus = generateFeedbackStatus();
        String title = generateTitle();
        String userName = generateUserName(context, ownFeedback);
        String technicalUserName = generateTechnicalUserName(context, ownFeedback);
        long timeStamp = generateTimestamp();
        int responses = generateResponses();
        boolean isSubscribed = new Random().nextBoolean();

        return new FeedbackBean.Builder(feedbackId)
                .withTitle(title)
                .withUserName(userName)
                .withTechnicalUserName(technicalUserName)
                .withTimestamp(timeStamp)
                .withVotes(generateFeedbackVotes(context, new Random().nextInt(maxVotes) + minVotes, feedbackId))
                .withResponses(responses)
                .withStatus(feedbackStatus)
                .withIsSubscribed(isSubscribed)
                .build();
    }

    private static FeedbackVoteBean generateFeedbackVote(Context context, String feedbackId) {
        int vote = generateVote();
        String userName = generateUserName(context, false);
        String technicalUserName = generateTechnicalUserName(context, true);
        long timeStamp = generateTimestamp();

        return new FeedbackVoteBean.Builder()
                .withVote(vote)
                .withUserName(userName)
                .withTechnicalUserName(technicalUserName)
                .withTimestamp(timeStamp)
                .withFeedbackId(feedbackId)
                .build();
    }

    private static FEEDBACK_STATUS generateFeedbackStatus() {
        FEEDBACK_STATUS[] status = new FEEDBACK_STATUS[]{OPEN, IN_PROGRESS, REJECTED, DUPLICATE, CLOSED};
        return status[NumberUtility.randomPosition(status)];
    }

    private static int generateVote() {
        return new Random().nextInt(2) * 2 - 1; // returns either -1 or 1
    }

    private static int generateResponses() {
        return NumberUtility.randomInt(0, 50);
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
