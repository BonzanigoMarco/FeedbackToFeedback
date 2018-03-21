package ch.uzh.supersede.feedbacklibrary.entrypoint;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import java.util.HashMap;

import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService.ConfigurationRequestWrapper;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService.ConfigurationRequestWrapper.ConfigurationRequestWrapperBuilder;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SUPERSEEDE_BASE_URL;

public class FeedbackConnector {
    HashMap<Integer,View> registeredViews;

    private static final FeedbackConnector instance = new FeedbackConnector();

    public static FeedbackConnector getInstance() {
        return instance;
    }

    private FeedbackConnector() {
        registeredViews = new HashMap<>();
    }

    public void connect(View view, Activity activity){
        if (!registeredViews.containsKey(view.getId())){
            registeredViews.put(view.getId(),view);
            view.setOnTouchListener(new FeedbackOnTouchListener(activity,view));
            onTouchConnector(activity, view, null);
        }
    }

    protected static void onTouchConnector(Activity activity, View view, MotionEvent event){
        if (event == null){ //On Listener attached
            Toast.makeText(activity.getApplicationContext(),"Feedback Functionality attached!", Toast.LENGTH_SHORT).show();
        }else{ //On Listener triggered
            Toast.makeText(activity.getApplicationContext(),"Feedback Functionality running!", Toast.LENGTH_SHORT).show();
        }
        //General
        boolean result = Utils.checkSinglePermission(activity, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest
                .permission.READ_EXTERNAL_STORAGE, null, null, false);
        if (result) {
            FeedbackService
                    .getInstance()
                    .startFeedbackActivityWithScreenshotCapture(SUPERSEEDE_BASE_URL, activity, 1337, "en");
        }
    }

    private static class FeedbackOnTouchListener implements OnTouchListener{
        private View mView;
        private Activity mActivity;
        protected FeedbackOnTouchListener(Activity activity, View view){
            this.mActivity = activity;
            this.mView = view;
        }
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            onTouchConnector(mActivity,mView,event);
            return false;
        }
    }
}