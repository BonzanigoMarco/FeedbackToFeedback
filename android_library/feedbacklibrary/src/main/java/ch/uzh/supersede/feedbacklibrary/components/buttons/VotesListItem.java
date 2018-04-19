package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.AbstractFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackVoteBean;

public class VotesListItem extends AbstractSettingsItem {

    public VotesListItem(Context context, int visibleTiles, AbstractFeedbackBean feedbackBean) {
        super(context, visibleTiles, feedbackBean);

        LinearLayout upperWrapperLayout = getUpperWrapperLayout();
        ImageView voteView = createVoteView(getShortParams(), context, feedbackBean, getColoredBackground(), PADDING);

        upperWrapperLayout.addView(voteView);
        addView(upperWrapperLayout);
        addView(getLowerWrapperLayout());
    }

    private ImageView createVoteView(LinearLayoutCompat.LayoutParams layoutParams, Context context, AbstractFeedbackBean feedbackBean, Drawable background, int padding) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);

        if (feedbackBean instanceof FeedbackVoteBean) {
            if (((FeedbackVoteBean) feedbackBean).getVote() == 1) {
                imageView.setImageResource(R.drawable.ic_thumb_up_black_48dp);
            } else {
                imageView.setImageResource(R.drawable.ic_thumb_down_black_48dp);
            }
        } else {
            imageView.setImageResource(R.drawable.button_selector_edit);
        }

        imageView.setBackground(background);
        imageView.setPadding(padding, padding, padding, padding);
        return imageView;
    }
}
