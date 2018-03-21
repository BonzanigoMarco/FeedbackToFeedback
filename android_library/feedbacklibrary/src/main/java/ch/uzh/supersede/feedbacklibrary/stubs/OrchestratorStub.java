package ch.uzh.supersede.feedbacklibrary.stubs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;
import ch.uzh.supersede.feedbacklibrary.views.AudioMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.CategoryMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.MechanismView;
import ch.uzh.supersede.feedbacklibrary.views.RatingMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.ScreenshotMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.ScreenshotMechanismView.OnImageChangedListener;
import ch.uzh.supersede.feedbacklibrary.views.TextMechanismView;

import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.ATTACHMENT_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.AUDIO_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.CATEGORY_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.DIALOG_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.IMAGE_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.RATING_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.SCREENSHOT_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.TEXT_TYPE;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.OrchestratorConstants.*;

/**
 * Created by Marco on 15.03.2018.
 */

public class OrchestratorStub {
    private ArrayList<MechanismView> mechanismViews;
    private OrchestratorStub() {
        mechanismViews = new ArrayList<>();
    }

    public List<MechanismView> getMechanismViews(){
        return this.mechanismViews;
    }


    public void addAll(LinearLayout linearLayout, List<MechanismView> mechanismViews) {
    }

    public static void receiveFeedback(Activity activity, View view) {
        //TODO: Evaluation, Store etc
        Toast toast = Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.supersede_feedbacklibrary_success_text), Toast.LENGTH_SHORT);
        toast.show();
    }

    public static class MechanismBuilder <T extends Activity & OnImageChangedListener> {
        private ArrayList<MechanismView> viewList;
        private Context context;
        private LayoutInflater layoutInflater;
        private LinearLayout rootLayout;
        private Resources resources;
        private T activity;
        private String imagePath;
        private int id;

        public  MechanismBuilder(T activity, Context context, Resources resources, LinearLayout rootLayout, LayoutInflater layoutInflater, String imagePath) {
            viewList = new ArrayList<>();
            id = 0;
            this.context = context;
            this.layoutInflater = layoutInflater;
            this.resources = resources;
            this.rootLayout = rootLayout;
            this.activity = activity;
            this.imagePath = imagePath;
        }

        @Deprecated
        public MechanismBuilder withAttachment() {
            resolve(ATTACHMENT_TYPE);
            return this;
        }

        public MechanismBuilder withAudio() {
            resolve(AUDIO_TYPE);
            return this;
        }

        public MechanismBuilder withCategory() {
            resolve(CATEGORY_TYPE);
            return this;
        }

        @Deprecated
        public MechanismBuilder withDialog() {
            resolve(DIALOG_TYPE);
            return this;
        }

        @Deprecated
        public MechanismBuilder withImage() {
            resolve(IMAGE_TYPE);
            return this;
        }

        public MechanismBuilder withRating() {
            resolve(RATING_TYPE);
            return this;
        }

        public MechanismBuilder withScreenshot() {
            resolve(SCREENSHOT_TYPE);
            return this;
        }

        public MechanismBuilder withText() {
            resolve(TEXT_TYPE);
            return this;
        }

        public OrchestratorStub build(List<MechanismView> mechanismViews) {
            OrchestratorStub stub = new OrchestratorStub();
            for (MechanismView view : viewList){
                mechanismViews.add(view);
                rootLayout.addView(view.getEnclosingLayout());
            }
            return stub;
        }

        private void resolve(String type) {
            MechanismConfigurationItem configurationItem = new MechanismConfigurationItem();
            configurationItem.setCanBeActivated(true);
            configurationItem.setActive(true);
            configurationItem.setOrder(id);
            configurationItem.setId(id++);
            switch (type) {
                case ATTACHMENT_TYPE:
                    //TODO: Gab es nie, implementieren!
                    break;
                case AUDIO_TYPE:
                    configurationItem.setType(AUDIO_TYPE);
                    configurationItem.setParameters(OrchestratorParamBuilder
                            .instance()
                            .key(AUDIO_TITLE_KEY)
                            .value(AUDIO_TITLE_VALUE)
                            .key(AUDIO_MAX_TIME_KEY)
                            .value(AUDIO_MAX_TIME_VALUE)
                            .get());
                    AudioMechanism audioMechanism = new AudioMechanism(configurationItem);
                    AudioMechanismView audioMechanismView = new AudioMechanismView(layoutInflater, audioMechanism, resources, activity, context);
                    viewList.add(audioMechanismView);
                    break;
                case CATEGORY_TYPE:
                    configurationItem.setType(CATEGORY_TYPE);
                    configurationItem.setParameters(OrchestratorParamBuilder
                            .instance()
                            .key(CATEGORY_TITLE_KEY)
                            .value(CATEGORY_TITLE_VALUE)
                            .key(CATEGORY_MANDATORY_KEY)
                            .value(CATEGORY_MANDATORY_VALUE)
                            .key(CATEGORY_MULTIPLE_KEY)
                            .value(CATEGORY_MULTIPLE_VALUE)
                            .key(CATEGORY_MANDATORY_REMINDER_KEY)
                            .value(CATEGORY_MANDATORY_REMINDER_VALUE)
                            .key(CATEGORY_OPTIONS_KEY)
                            .value(CATEGORY_OPTIONS_VALUE)
                            .key(CATEGORY_OWN_KEY)
                            .value(CATEGORY_OWN_VALUE)
                            .get());
                    CategoryMechanism categoryMechanism = new CategoryMechanism(configurationItem);
                    CategoryMechanismView categoryMechanismView = new CategoryMechanismView(layoutInflater, categoryMechanism);
                    viewList.add(categoryMechanismView);
                    break;
                case DIALOG_TYPE:
                    //TODO: Gab es nie, implementieren!
                    break;
                case IMAGE_TYPE:
                    //TODO: Gab es nie, implementieren!
                    break;
                case RATING_TYPE:
                    configurationItem.setType(RATING_TYPE);
                    configurationItem.setParameters(OrchestratorParamBuilder
                            .instance()
                            .key(RATING_TITLE_KEY)
                            .value(RATING_TITLE_VALUE)
                            .key(RATING_ICON_KEY)
                            .value(RATING_ICON_VALUE)
                            .key(RATING_MAX_KEY)
                            .value(RATING_MAX_VALUE)
                            .key(RATING_DEFAULT_KEY)
                            .value(RATING_DEFAULT_VALUE)
                            .get());
                    RatingMechanism ratingMechanism = new RatingMechanism(configurationItem);
                    RatingMechanismView ratingMechanismView = new RatingMechanismView(layoutInflater, ratingMechanism);
                    viewList.add(ratingMechanismView);
                    break;
                case SCREENSHOT_TYPE:
                    configurationItem.setType(SCREENSHOT_TYPE);
                    configurationItem.setParameters(OrchestratorParamBuilder
                            .instance()
                            .key(SCREENSHOT_TITLE_KEY)
                            .value(SCREENSHOT_TITLE_VALUE)
                            .key(SCREENSHOT_DEFAULT_KEY)
                            .value(SCREENSHOT_DEFAULT_VALUE)
                            .key(SCREENSHOT_MAX_TEXT_KEY)
                            .value(SCREENSHOT_MAX_TEXT_VALUE)
                            .get());
                    ScreenshotMechanism screenshotMechanism = new ScreenshotMechanism(configurationItem);
                    ScreenshotMechanismView screenshotMechanismView = new ScreenshotMechanismView(layoutInflater, screenshotMechanism, activity, id, imagePath);
                    viewList.add(screenshotMechanismView);
                    break;
                case TEXT_TYPE:
                    configurationItem.setType(TEXT_TYPE);
                    configurationItem.setParameters(OrchestratorParamBuilder
                            .instance()
                            .key(TEXT_TITLE_KEY)
                            .value(TEXT_TITLE_VALUE)
                            .key(TEXT_HINT_KEY)
                            .value(TEXT_HINT_VALUE)
                            .key(TEXT_LABEL_KEY)
                            .value(TEXT_LABEL_VALUE)
                            .key(TEXT_FONT_COLOR_KEY)
                            .value(TEXT_FONT_COLOR_VALUE)
                            .key(TEXT_FONT_SIZE_KEY)
                            .value(TEXT_FONT_SIZE_VALUE)
                            .key(TEXT_FONT_TYPE_KEY)
                            .value(TEXT_FONT_TYPE_VALUE)
                            .key(TEXT_ALIGN_KEY)
                            .value(TEXT_ALIGN_VALUE)
                            .key(TEXT_MAX_LENGTH_KEY)
                            .value(TEXT_MAX_LENGTH_VALUE)
                            .key(TEXT_MAX_LENGTH_VISIBLE_KEY)
                            .value(TEXT_MAX_LENGTH_VISIBLE_VALUE)
                            .key(TEXT_LENGTH_VISIBLE_KEY)
                            .value(TEXT_LENGTH_VISIBLE_VALUE)
                            .key(TEXT_MANDATORY_KEY)
                            .value(TEXT_MANDATORY_VALUE)
                            .key(TEXT_REMINDER_KEY)
                            .value(TEXT_REMINDER_VALUE)
                            .get());
                    TextMechanism textMechanism = new TextMechanism(configurationItem);
                    TextMechanismView textMechanismView = new TextMechanismView(layoutInflater, textMechanism);
                    viewList.add(textMechanismView);
                    break;
                default:
                    break;
            }
        }
    }

    private static class OrchestratorParamBuilder {
        private List<Map<String, Object>> list;

        private OrchestratorParamBuilder() {
            this.list = new ArrayList<>();
        }

        public OrchestratorParamBuilder(List<Map<String, Object>> list) {
            this.list = list;
        }

        public static OrchestratorParamBuilder instance() {
            return new OrchestratorParamBuilder();
        }

        public Builder key(Object value) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ORCHESTRATOR_KEY, value);
            return new Builder(list, map);
        }

        public List<Map<String, Object>> get() {
            return list;
        }

        private static class Builder {
            private List<Map<String, Object>> list;
            HashMap<String, Object> map;

            public Builder(List<Map<String, Object>> list, HashMap<String, Object> map) {
                this.list = list;
                this.map = map;
            }

            public OrchestratorParamBuilder value(Object value) {
                HashMap<String, Object> map = new HashMap<>();
                map.put(ORCHESTRATOR_VALUE, value);
                list.add(map);
                return new OrchestratorParamBuilder(list);
            }
        }
    }
}