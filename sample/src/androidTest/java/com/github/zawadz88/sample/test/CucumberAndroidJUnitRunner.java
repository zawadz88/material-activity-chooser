package com.github.zawadz88.sample.test;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;
import android.text.TextUtils;

import cucumber.api.android.CucumberInstrumentationCore;

/**
 * A {@link AndroidJUnitRunner} which runs Cucumber scenarios.
 *
 * @author Piotr Zawadzki
 */
public class CucumberAndroidJUnitRunner extends AndroidJUnitRunner {

    private static final String CUCUMBER_INTERNAL_TAGS_KEY = "tags";

    private CucumberInstrumentationCore cucumberInstrumentationCore = new CucumberInstrumentationCore(this);

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        String tags = getTags(bundle);
        if (!TextUtils.isEmpty(tags)) {
            tags = tags.replaceAll("\\s","");
            bundle.putString(CUCUMBER_INTERNAL_TAGS_KEY, tags);
        }
        this.cucumberInstrumentationCore.create(bundle);
    }

    @Override
    public void onStart() {
        waitForIdleSync();
        this.cucumberInstrumentationCore.start();
    }

    /**
     * Gets the Cucumber tags which were passes to the instrumentation either via Gradle properties
     * or directly to <pre>adb shell am instrument</pre>
     * @param bundle instrumentation bundle
     * @return Cucumber tags
     */
    private String getTags(Bundle bundle) {
        return bundle.containsKey(CUCUMBER_INTERNAL_TAGS_KEY)
                ? bundle.getString(CUCUMBER_INTERNAL_TAGS_KEY)
                : BuildConfig.CUCUMBER_TAGS;
    }
}
