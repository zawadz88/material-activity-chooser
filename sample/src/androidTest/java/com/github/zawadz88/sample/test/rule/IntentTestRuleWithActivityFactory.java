package com.github.zawadz88.sample.test.rule;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.MonitoringInstrumentation;
import android.support.test.runner.intercepting.SingleActivityFactory;

/**
 * An {@link IntentsTestRule} with an activity factory.
 *
 * @author Piotr Zawadzki
 * @see ActivityTestRule#ActivityTestRule(SingleActivityFactory, boolean, boolean)
 * @see SingleActivityFactory
 */
public class IntentTestRuleWithActivityFactory<ACTIVITY extends Activity> extends ActivityTestRule<ACTIVITY> {

    private SingleActivityFactory<ACTIVITY> mActivityFactory;

    public IntentTestRuleWithActivityFactory(SingleActivityFactory<ACTIVITY> activityFactory, boolean initialTouchMode, boolean launchActivity) {
        super(activityFactory, initialTouchMode, launchActivity);
        this.mActivityFactory = activityFactory;
    }

    @Override
    protected void afterActivityLaunched() {
        Intents.init();
        super.afterActivityLaunched();
    }

    @Override
    protected void afterActivityFinished() {
        super.afterActivityFinished();
        Intents.release();
    }

    @Override
    public ACTIVITY launchActivity(@Nullable Intent startIntent) {
        MonitoringInstrumentation instrumentation = ((MonitoringInstrumentation) InstrumentationRegistry.getInstrumentation());
        if (mActivityFactory != null) {
            instrumentation.interceptActivityUsing(mActivityFactory);
        } else {
            instrumentation.useDefaultInterceptingActivityFactory();
        }
        return super.launchActivity(startIntent);
    }

}
