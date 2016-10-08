package com.github.zawadz88.sample.test.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Contains common Activity operations.
 *
 * @author Piotr Zawadzki
 */
public final class ActivityUtils {

    /**
     * Finishes all currently opened activities belonging to this process started by this instrumentation.
     */
    public static void finishOpenActivities() {
        new Handler(Looper.getMainLooper()).post(new ActivityFinisher());
    }

    /**
     * Gets an instance of the currently active (displayed) activity.
     * @param activityTestRule test rule
     * @param <T> activity class
     * @return activity instance
     */
    public static  <T extends Activity> T getCurrentActivity(@NonNull ActivityTestRule activityTestRule) {
        getInstrumentation().waitForIdleSync();
        final Activity[] activity = new Activity[1];
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    java.util.Collection<Activity> activites = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                    activity[0] = Iterables.getOnlyElement(activites);
                }});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //noinspection unchecked
        return (T) activity[0];
    }
}
