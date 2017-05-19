package com.customasynctask.com;

import android.app.Activity;
import android.app.Application;

import com.customasynctask.com.Helper.AsyncTaskHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 19-05-2017.
 */

public class CustomAsyncTaskApplication extends Application {

    public AsyncTaskHelper mAsyncTaskHelper ;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initializing AsyncTaskHelper
        mAsyncTaskHelper = AsyncTaskHelper.init();
    }


}
