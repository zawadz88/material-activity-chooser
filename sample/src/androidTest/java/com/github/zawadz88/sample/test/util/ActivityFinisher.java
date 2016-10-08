package com.github.zawadz88.sample.test.util;

/**
 * @author Piotr Zawadzki
 */


import android.app.Activity;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Loops through all the activities that have not yet finished and explicitly calls finish
 * on them.
 * @see android.support.test.runner.MonitoringInstrumentation.ActivityFinisher
 */
class ActivityFinisher implements Runnable {

    private static final String TAG = "ActivityFinisher";

    private final ActivityLifecycleMonitor mLifecycleMonitor;

    ActivityFinisher() {
        this.mLifecycleMonitor = ActivityLifecycleMonitorRegistry.getInstance();
    }


    @Override
    public void run() {
        List<Activity> activities = new ArrayList<>();

        for (Stage s : EnumSet.range(Stage.CREATED, Stage.STOPPED)) {
            activities.addAll(mLifecycleMonitor.getActivitiesInStage(s));
        }

        Log.i(TAG, "Activities that are still in CREATED to STOPPED: " + activities.size());

        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                try {
                    Log.i(TAG, "Finishing mActivityRule: " + activity);
                    activity.finish();
                } catch (RuntimeException e) {
                    Log.e(TAG, "Failed to finish mActivityRule.", e);
                }
            }
        }
    }
};
