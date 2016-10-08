package com.github.zawadz88.sample.test.util;

import android.view.View;

import com.github.zawadz88.sample.test.viewmatcher.RecyclerViewColumnMatcher;
import com.github.zawadz88.sample.test.viewmatcher.RecyclerViewMatcher;

import org.hamcrest.Matcher;

/**
 * @author Piotr Zawadzki
 */
public final class ViewMatchers {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static Matcher<View> withColumnCount(final int columnCount) {
        return new RecyclerViewColumnMatcher(columnCount);
    }
}
