package ch.uzh.supersede.host;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.activities.AbstractBaseActivity;
import ch.uzh.supersede.feedbacklibrary.entrypoint.*;

public class HostActivity extends AbstractBaseActivity implements IFeedbackBehavior, IFeedbackStyle, IFeedbackSettings, IFeedbackLayoutConfiguration, IFeedbackDeveloper {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        LinearLayout linearLayout = getView(R.id.root_layout,LinearLayout.class);
        Button feedbackButton = getView(R.id.button_host_feedback,Button.class);
        Button dummyButton1 = getView(R.id.button_host_trigger_1,Button.class);
        Button dummyButton2 = getView(R.id.button_host_trigger_2,Button.class);
        CoordinatorLayout cLayout = getView(R.id.coordinator_layout,CoordinatorLayout.class);
        double primaryColorDouble = ((double)Color.BLACK)*Math.random();
        int primaryColor = (int)primaryColorDouble;
        linearLayout.setBackgroundColor(primaryColor);
        cLayout.setBackgroundColor(primaryColor);

        double secondaryColorDouble = ((double)Color.BLACK)*Math.random();
        int secondaryColor = (int)secondaryColorDouble;
        feedbackButton.setBackgroundColor(secondaryColor);
        dummyButton1.setBackgroundColor(secondaryColor);
        dummyButton2.setBackgroundColor(secondaryColor);
    }

    public void onFeedbackClicked(View view){
        FeedbackConnector.getInstance().connect(view,this);
    }

    //Feedback Layout
    @Override
    public int getConfiguredAudioFeedbackOrder() {
        return -1;
    }

    @Override
    public int getConfiguredScreenshotFeedbackOrder() {
        return 2;
    }

    @Override
    public int getConfiguredCategoryFeedbackOrder() {
        return -1;
    }

    @Override
    public int getConfiguredTextFeedbackOrder() {
        return -1;
    }

    @Override
    public int getConfiguredRatingFeedbackOrder() {
        return 1;
    }

    //Feedback Settings
    @Override
    public int getConfiguredMinUserNameLength() {
        return 3;
    }

    @Override
    public int getConfiguredMaxUserNameLength() {
        return 10;
    }

    @Override
    public int getConfiguredMinResponseLength() {
        return 10;
    }

    @Override
    public int getConfiguredMaxResponseLength() {
        return 30;
    }

    //Feedback Developer
    @Override
    public boolean isDeveloper() {
        return true;
    }

    //Feedback Behaviour
    @Override
    public int getConfiguredPullIntervalMinutes() {
        return 0;
    }

    //Feedback Layout
    @Override
    public FEEDBACK_STYLE getConfiguredFeedbackStyle() {
        return FEEDBACK_STYLE.ADAPTIVE;
    }
}