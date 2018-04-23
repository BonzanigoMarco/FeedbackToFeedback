package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackVoteBean;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;

public class VotesListItem extends AbstractSettingsListItem {

    public VotesListItem(Context context, int visibleTiles, FeedbackVoteBean feedbackVoteBean) {
        super(context, visibleTiles, RepositoryStub.getFeedback(context, feedbackVoteBean.getFeedbackId()));

        LinearLayout upperWrapperLayout = getUpperWrapperLayout();
        LinearLayout lowerWrapperLayout = getLowerWrapperLayout();

        FeedbackBean feedback = RepositoryStub.getFeedback(context, feedbackVoteBean.getFeedbackId());

        TextView titleView = createTextView(getShortParams(), context, feedbackVoteBean.getUserName().concat(": " + feedback.getTitle()), Gravity.START, getColoredBackground(), PADDING, getTextColor());
        ImageView voteView = createVoteView(getShortParams(), context, feedbackVoteBean, getColoredBackground(), PADDING);

        upperWrapperLayout.addView(titleView);
        upperWrapperLayout.addView(getDateView());

        lowerWrapperLayout.addView(voteView);
        addView(getUpperWrapperLayout());
        addView(lowerWrapperLayout);
    }

    private ImageView createVoteView(LinearLayoutCompat.LayoutParams layoutParams, Context context, FeedbackVoteBean feedbackVoteBean, Drawable background, int padding) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);

        if (feedbackVoteBean.getVote() > 0) {
            imageView.setImageResource(R.drawable.ic_thumb_up_black_48dp);
            imageView.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.cyan), PorterDuff.Mode.SRC_IN);
        } else {
            imageView.setImageResource(R.drawable.ic_thumb_down_black_48dp);
            imageView.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_IN);
        }

        imageView.setBackground(background);
        imageView.setPadding(padding, padding, padding, padding);

        return imageView;
    }
}
