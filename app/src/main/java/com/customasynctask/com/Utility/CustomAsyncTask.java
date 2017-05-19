package com.customasynctask.com.Utility;

import android.app.Activity;
import android.os.AsyncTask;

import com.customasynctask.com.CustomAsyncTaskApplication;

/**
 * Created by Admin on 19-05-2017.
 */

public abstract class CustomAsyncTask<TParams, TProgress, TResult> extends AsyncTask<TParams, TProgress, TResult> {
    protected CustomAsyncTaskApplication mAppInstance;
    protected Activity mActivity;

    public CustomAsyncTask(Activity activity) {
        mActivity = activity;
        mAppInstance = (CustomAsyncTaskApplication) mActivity.getApplication();
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
        if (mActivity == null) {
            onActivityDetached();
        }
        else {
            onActivityAttached();
        }
    }

    protected void onActivityAttached() {}

    protected void onActivityDetached() {}

    @Override
    protected void onPreExecute() {
        mAppInstance.mAsyncTaskHelper.addTask(mActivity, this);
    }

    @Override
    protected void onPostExecute(TResult result) {
        mAppInstance.mAsyncTaskHelper.removeTask(this);
    }

    @Override
    protected void onCancelled() {
        mAppInstance.mAsyncTaskHelper.removeTask(this);
    }
}