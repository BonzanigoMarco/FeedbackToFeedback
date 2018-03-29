package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.utils.ObjectUtility;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class AbstractBaseActivity extends AppCompatActivity {

    protected <T> T getView(int id, Class<T> classType) {
        return classType.cast(findViewById(id));
    }

    protected void onPostCreate(){
        invokeNullSafe(getSupportActionBar(),"hide",null);
    }

    public void onButtonClicked(View view){
        Toast.makeText(getApplicationContext(),"Button Clicked.",Toast.LENGTH_SHORT).show();
    }

    protected <T extends Activity> void  startActivity(T startActivity, Class<?> activityToStart ){
        Intent intent = new Intent(startActivity.getApplicationContext(), activityToStart);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity.startActivity(intent);
        startActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected Object invokeNullSafe(Object o, String methodName, Object valueIfNull, Object... params){
        if (o==null){
            return valueIfNull;
        }
        Class[] paramClasses = new Class[params!=null?params.length:0];
        for (int p = 0; p < (params!=null?params.length:0); p++){
            paramClasses[p]=params[p].getClass();
        }
        Object returnObject = null;
        try {
            Method method = (params==null?o.getClass().getMethod(methodName):o.getClass().getMethod(methodName,paramClasses));
            if (method==null){
                return valueIfNull;
            }
            returnObject = method.invoke(o, params);
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException |InvocationTargetException e) {
            Log.e("invokeNullSafe",e.getMessage());
        }
        return ObjectUtility.nvl(returnObject,valueIfNull);
    }
}
