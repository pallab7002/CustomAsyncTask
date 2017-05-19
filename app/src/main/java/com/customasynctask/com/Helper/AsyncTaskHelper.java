package com.customasynctask.com.Helper;

import android.app.Activity;

import com.customasynctask.com.Utility.CustomAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 19-05-2017.
 */

public class AsyncTaskHelper {

    private Map<String, List<CustomAsyncTask<?,?,?>>> mActivityTaskMap;

    private static AsyncTaskHelper mAsyncTaskHelper = null;

    private AsyncTaskHelper(){
        mActivityTaskMap = new HashMap<String, List<CustomAsyncTask<?,?,?>>>();
    }

    public static AsyncTaskHelper init(){
        if (mAsyncTaskHelper == null)
            mAsyncTaskHelper = new AsyncTaskHelper();

        return mAsyncTaskHelper;
    }

    public void removeTask(CustomAsyncTask<?,?,?> task) {
        for (Map.Entry<String, List<CustomAsyncTask<?,?,?>>> entry : mActivityTaskMap.entrySet()) {
            List<CustomAsyncTask<?,?,?>> tasks = entry.getValue();
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i) == task) {
                    tasks.remove(i);
                    break;
                }
            }

            if (tasks.size() == 0) {
                mActivityTaskMap.remove(entry.getKey());
                return;
            }
        }
    }

    public void addTask(Activity activity, CustomAsyncTask<?,?,?> task) {
        String key = activity.getClass().getCanonicalName();
        List<CustomAsyncTask<?,?,?>> tasks = mActivityTaskMap.get(key);
        if (tasks == null) {
            tasks = new ArrayList<CustomAsyncTask<?,?,?>>();
            mActivityTaskMap.put(key, tasks);
        }

        tasks.add(task);
    }

    public void detach(Activity activity) {
        List<CustomAsyncTask<?,?,?>> tasks = mActivityTaskMap.get(activity.getClass().getCanonicalName());
        if (tasks != null) {
            for (CustomAsyncTask<?,?,?> task : tasks) {
                task.setActivity(null);
            }
        }
    }

    public void attach(Activity activity) {
        List<CustomAsyncTask<?,?,?>> tasks = mActivityTaskMap.get(activity.getClass().getCanonicalName());
        if (tasks != null) {
            for (CustomAsyncTask<?,?,?> task : tasks) {
                task.setActivity(activity);
            }
        }
    }
}
