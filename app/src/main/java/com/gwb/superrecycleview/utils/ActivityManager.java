package com.gwb.superrecycleview.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${GongWenbo} on 2018/5/23 0023.
 */
public class ActivityManager {

    private static ActivityManager sActivityManager;
    private List<Activity> mActivities = new ArrayList<>();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (sActivityManager == null) {
            synchronized (ActivityManager.class) {
                if (sActivityManager == null) {
                    sActivityManager = new ActivityManager();
                }
            }
        }
        return sActivityManager;
    }

    public List<Activity> getActivities() {
        return mActivities;
    }

    public void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void finish(Activity activity) {
        mActivities.remove(activity);
    }

    public void finishAll() {
        for (int i = 0; i < mActivities.size(); i++) {
            Activity activity = mActivities.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
            mActivities.remove(activity);
            i--;
        }
    }
}
