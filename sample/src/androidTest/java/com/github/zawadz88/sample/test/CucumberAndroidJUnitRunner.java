package com.github.zawadz88.sample.test;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

import cucumber.api.android.CucumberInstrumentationCore;

/**
 * A {@link AndroidJUnitRunner} which runs Cucumber scenarios.
 *
 * @author Piotr Zawadzki
 */
public class CucumberAndroidJUnitRunner extends AndroidJUnitRunner {

    private CucumberInstrumentationCore cucumberInstrumentationCore = new CucumberInstrumentationCore(this);

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.cucumberInstrumentationCore.create(bundle);
    }

    @Override
    public void onStart() {
        waitForIdleSync();
        this.cucumberInstrumentationCore.start();
    }
}
