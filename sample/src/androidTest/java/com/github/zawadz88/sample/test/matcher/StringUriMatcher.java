package com.github.zawadz88.sample.test.matcher;

import android.net.Uri;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Piotr Zawadzki
 */
public class StringUriMatcher extends TypeSafeMatcher<Uri> {

    private Matcher<String> mStringUriMatcher;

    public StringUriMatcher(Matcher<String> stringUriMatcher) {
        this.mStringUriMatcher = stringUriMatcher;
    }

    @Override
    protected boolean matchesSafely(Uri item) {
        if (item == null) {
            return mStringUriMatcher == null;
        }
        return mStringUriMatcher.matches(item.toString());
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(mStringUriMatcher);
    }

    public static Matcher<Uri> withStringUri(Matcher<String> stringUriMatcher) {
        return new StringUriMatcher(stringUriMatcher);
    }
}
