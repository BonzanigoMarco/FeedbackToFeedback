package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackDetailsActivity;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.utils.DateUtility;
import ch.uzh.supersede.feedbacklibrary.utils.NumberUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_FEEDBACK_BEAN;

public abstract class AbstractSettingsListItem extends LinearLayout {
    public static final int PADDING = 10;
    private FeedbackBean feedbackBean;
    private Drawable coloredBackground;
    private LinearLayout upperWrapperLayout;
    private LinearLayout lowerWrapperLayout;
    private TextView dateView;
    private TextView titleView;
    private int textColor;

    public FeedbackBean getFeedbackBean() {
        return feedbackBean;
    }

    public LinearLayoutCompat.LayoutParams getShortParams() {
        return shortParams;
    }

    private LinearLayoutCompat.LayoutParams shortParams;

    public LinearLayout getUpperWrapperLayout() {
        return upperWrapperLayout;
    }

    public LinearLayout getLowerWrapperLayout() {
        return lowerWrapperLayout;
    }

    public Drawable getColoredBackground() {
        return coloredBackground;
    }

    public TextView getDateView() {
        return dateView;
    }

    public int getTextColor() {
        return textColor;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public AbstractSettingsListItem(Context context, int visibleTiles, FeedbackBean feedbackBean) {
        super(context);
        this.feedbackBean = feedbackBean;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                                 .getDefaultDisplay()
                                 .getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int partHeight = NumberUtility.divide(screenHeight, visibleTiles + 3);
        int innerLayoutWidth = NumberUtility.multiply(screenWidth, 0.905f); //weighted 20/22
        LayoutParams masterParams = new LayoutParams(screenWidth, partHeight);
        masterParams.setMargins(5, 5, 5, 5);
        setLayoutParams(masterParams);
        setOrientation(VERTICAL);
        LinearLayoutCompat.LayoutParams longParams = new LinearLayoutCompat.LayoutParams(screenWidth, partHeight / 2);
        shortParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth / 2, partHeight / 2);
        coloredBackground = ContextCompat.getDrawable(context, R.drawable.dark_blue_square);
        textColor = ContextCompat.getColor(context, R.color.white);
        upperWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        lowerWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);

        titleView = createTextView(getShortParams(), context, feedbackBean.getUserName().concat(": " + feedbackBean.getTitle()), Gravity.START, getColoredBackground(), PADDING, getTextColor());
        dateView = createTextView(shortParams, context, context.getString(R.string.list_date, DateUtility.getDateFromLong(feedbackBean.getTimeStamp())), Gravity.END, coloredBackground, PADDING,
                textColor);
        TextView statusView = createTextView(shortParams, context, feedbackBean.getFeedbackStatus().getLabel(),
                Gravity.START, coloredBackground, PADDING, feedbackBean.getFeedbackStatus().getColor());

        lowerWrapperLayout.addView(statusView);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startFeedbackDetailsActivity();
                return false;
            }
        });
    }

    private void startFeedbackDetailsActivity() {
        Intent intent = new Intent(getContext(), FeedbackDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_KEY_FEEDBACK_BEAN, feedbackBean);
        getContext().startActivity(intent);
        ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private LinearLayout createWrapperLayout(LinearLayoutCompat.LayoutParams layoutParams, Context context, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }

    protected TextView createTextView(LinearLayoutCompat.LayoutParams layoutParams, Context context, String text, int gravity, Drawable background, int padding, int textColor) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setBackground(background);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(textColor);
        return textView;
    }
}
