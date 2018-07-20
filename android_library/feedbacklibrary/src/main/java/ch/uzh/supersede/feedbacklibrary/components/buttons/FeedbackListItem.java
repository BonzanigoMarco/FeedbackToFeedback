package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.Date;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackDetailsActivity;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackDetailsDeveloperActivity;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.interfaces.ISortableFeedback;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_SORTING.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.DUPLICATE;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class FeedbackListItem extends LinearLayout implements Comparable, ISortableFeedback {
    private TextView titleView;
    private TextView dateView;
    private TextView statusView;
    private TextView pointView;
    private Feedback feedback;
    private Enums.FEEDBACK_SORTING sorting = NONE;
    private String ownUser = USER_NAME_ANONYMOUS;
    private LocalConfigurationBean configuration;

    public FeedbackListItem(Context context, int visibleTiles, Feedback feedback, LocalConfigurationBean configuration, int backgroundColor) {
        super(context);
        this.configuration = configuration;
        this.feedback = feedback;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                                 .getDefaultDisplay()
                                 .getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int padding = 10;
        int partHeight = NumberUtility.divide(screenHeight, visibleTiles + 3);
        int innerLayoutWidth = NumberUtility.multiply(screenWidth, 0.905f); //weighted 20/22
        LinearLayoutCompat.LayoutParams masterParams = new LinearLayoutCompat.LayoutParams(screenWidth, partHeight);
        masterParams.setMargins(5, 5, 5, 5);
        setLayoutParams(masterParams);
        setOrientation(VERTICAL);
        LinearLayoutCompat.LayoutParams longParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth, partHeight / 2);
        LinearLayoutCompat.LayoutParams shortParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth / 2, partHeight / 2);
        int textColor = ColorUtility.getTextColor(context, backgroundColor);
        LinearLayout upperWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        LinearLayout lowerWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        if (ACTIVE.check(context)) {
            ownUser = FeedbackDatabase.getInstance(getContext()).readString(USER_NAME, null);
        }
        titleView = createTextView(shortParams, context, feedback.getTitle(), Gravity.START, padding, textColor);
        dateView = createTextView(shortParams, context, context.getString(R.string.list_date, getFeedback().getCreatedAt()), Gravity.END, padding, textColor);
        int statusColor = ColorUtility.adjustColorToBackground(backgroundColor,feedback.getFeedbackStatus().getColor(),0.4);
        statusView = createTextView(shortParams, context, feedback.getFeedbackStatus().getLabel()
                .concat(SPACE + context.getString(R.string.list_responses, getFeedback().getResponses())), Gravity.START, padding, statusColor);
        pointView = createTextView(shortParams, context, String.valueOf(feedback.getVotes()), Gravity.END, padding, textColor);
        updatePercentageColor();
        setBackgroundColor(backgroundColor);
        upperWrapperLayout.addView(titleView);
        upperWrapperLayout.addView(dateView);
        lowerWrapperLayout.addView(statusView);
        lowerWrapperLayout.addView(pointView);
        addView(upperWrapperLayout);
        addView(lowerWrapperLayout);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (VersionUtility.getDateVersion() > 2) {
                    startFeedbackDetailsActivity();
                }
                return false;

            }
        });
    }

    private void startFeedbackDetailsActivity() {
        Intent intent = null;
        if (ACTIVE.check(getContext())){
            if (VersionUtility.getDateVersion()>=4 && FeedbackDatabase.getInstance(getContext()).readBoolean(USER_IS_DEVELOPER,false)){
                intent = new Intent(getContext(), FeedbackDetailsDeveloperActivity.class);
            }else{
                intent = new Intent(getContext(), FeedbackDetailsActivity.class);
            }
        }else {
            Toast.makeText(getContext(),R.string.list_alert_user_level,Toast.LENGTH_SHORT).show();
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_KEY_FEEDBACK_DETAIL_BEAN, FeedbackUtility.feedbackToFeedbackDetailsBean(getContext(), feedback));
        intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
        getContext().startActivity(intent);
        ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public Feedback getFeedback() {
        return feedback;
    }

    private LinearLayout createWrapperLayout(LinearLayoutCompat.LayoutParams layoutParams, Context context, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }

    private TextView createTextView(LinearLayoutCompat.LayoutParams layoutParams, Context context, String text, int gravity,int padding, int textColor) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(textColor);
        return textView;
    }

    @Override
    @SuppressWarnings({"squid:S3358", "squid:S1210", "squid:S3776"})
    public int compareTo(@NonNull Object o) {
        if (o instanceof FeedbackListItem) {
            if (sorting == MINE || sorting == NEW) {
                Date comparedTimestamp = ((FeedbackListItem) o).getFeedback().getCreatedAt();
                return comparedTimestamp.after(getFeedback().getCreatedAt()) ? 1 : comparedTimestamp == getFeedback().getCreatedAt() ? 0 : -1;
            } else if (sorting == HOT) {
                int comparedResponses = ((FeedbackListItem) o).getFeedback().getResponses();
                return comparedResponses > getFeedback().getResponses() ? 1 : comparedResponses == getFeedback().getResponses() ? 0 : -1;
            } else if (sorting == TOP) {
                int comparedUpVotes = ((FeedbackListItem) o).getFeedback().getVotes();
                return comparedUpVotes > getFeedback().getVotes() ? 1 : comparedUpVotes == getFeedback().getVotes() ? 0 : -1;
            }
        }
        return 0;
    }

    public void updatePercentageColor() {
        float percent;
        if (getFeedback().getVotes() < 0) {
            percent = 1f / (2 * getFeedback().getVotes()) * (getFeedback().getMinVotes() - getFeedback().getVotes());
        } else if (getFeedback().getVotes() == 0) {
            pointView.setTextColor(DUPLICATE.getColor());
            return;
        } else {
            percent = 1f / (2 * getFeedback().getMaxVotes()) * (getFeedback().getMaxVotes() + getFeedback().getVotes());
        }
        pointView.setTextColor(ColorUtility.percentToColor(percent));
    }

    @Override
    public void setSorting(Enums.FEEDBACK_SORTING sorting, List<Enums.FEEDBACK_STATUS> allowedStatuses) {
        if (sorting != MINE || StringUtility.equals(getFeedback().getUserIdentification(), ownUser)) {
            this.setVisibility(GONE);
            for (Enums.FEEDBACK_STATUS status : allowedStatuses){
                if (status == getFeedback().getFeedbackStatus()){
                    this.setVisibility(VISIBLE);
                }
            }
        } else {
            this.setVisibility(GONE);
        }
        this.sorting = sorting;
    }
}
