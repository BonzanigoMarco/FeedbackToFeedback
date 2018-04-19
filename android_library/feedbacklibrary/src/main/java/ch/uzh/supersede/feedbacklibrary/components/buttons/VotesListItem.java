package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.content.Context;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;

public class VotesListItem extends AbstractSettingsItem {
    private FeedbackBean feedbackBean;

    public VotesListItem(Context context, int visibleTiles, FeedbackBean feedbackBean) {
        super(context, visibleTiles, feedbackBean);
    }

}
