package ch.uzh.supersede.feedbacklibrary.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.components.buttons.AbstractSettingsItem;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackMockService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackMockServiceEventListener;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackSettingsActivity extends AbstractBaseActivity implements IFeedbackMockServiceEventListener {

    private LinearLayout scrollListLayout;

    private Button myButton;
    private Button othersButton;
    private Button settingsButton;

    private ArrayList<AbstractSettingsItem> myFeedbackList = new ArrayList<>();
    private ArrayList<AbstractSettingsItem> othersFeedbackList = new ArrayList<>();
    private ArrayList<AbstractSettingsItem> settingsFeedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_settings);

        scrollListLayout = getView(R.id.settings_layout_scroll, LinearLayout.class);

        myButton = setOnClickListener(getView(R.id.settings_button_mine, Button.class));
        othersButton = setOnClickListener(getView(R.id.settings_button_others, Button.class));
        settingsButton = setOnClickListener(getView(R.id.settings_button_settings, Button.class));

        execFillFeedbackList();
        toggleButtons(myButton);
        onPostCreate();
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case GET_MINE_FEEDBACK_VOTES:
                myFeedbackList = (ArrayList<AbstractSettingsItem>) response;
                break;
            case GET_OTHERS_FEEDBACK_VOTES:
                othersFeedbackList = (ArrayList<AbstractSettingsItem>) response;
                break;
            case GET_SETTINGS_FEEDBACK:
                settingsFeedbackList = (ArrayList<AbstractSettingsItem>) response;
                break;
            default:
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        //TODO [jfo]
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        //TODO [jfo]
    }

    private void execFillFeedbackList() {
        FeedbackMockService.getInstance().getOthersFeedbackVotes(this, this);
        FeedbackMockService.getInstance().getMineFeedbackVotes(this, this);
        FeedbackMockService.getInstance().getFeedbackSettings(this, this);
    }

    private Button setOnClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtons(v);
            }
        });
        return button;
    }

    private void toggleButtons(View v) {
        setInactive(myButton, othersButton, settingsButton);
        v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blue_tab));
        ((Button) v).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        if (v.getId() == myButton.getId()) {
            load(myFeedbackList);
        } else if (v.getId() == othersButton.getId()) {
            load(othersFeedbackList);
        } else if (v.getId() == settingsButton.getId()) {
            load(settingsFeedbackList);
        }
    }

    private void setInactive(Button... buttons) {
        for (Button b : buttons) {
            b.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gray_tab));
            b.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.indigo));
        }
    }

    private void load(List<AbstractSettingsItem> currentList) {
        scrollListLayout.removeAllViews();
        getView(R.id.settings_view_scroll, ScrollView.class).scrollTo(0, 0);
        for (AbstractSettingsItem item : currentList) {
            scrollListLayout.addView(item);
        }
    }
}
