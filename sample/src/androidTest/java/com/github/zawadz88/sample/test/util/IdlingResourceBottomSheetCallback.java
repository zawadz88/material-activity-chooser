package com.github.zawadz88.sample.test.util;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.view.View;

/**
 * A wrapper for {@link BottomSheetBehavior.BottomSheetCallback} which also handles idling resources.
 *
 * @author Piotr Zawadzki
 */
public class IdlingResourceBottomSheetCallback extends BottomSheetBehavior.BottomSheetCallback {

    private BottomSheetBehavior.BottomSheetCallback mWrappedCallback;

    /**
     * Flag indicating if there is a swipe action triggered explicitly i.e. via a {@link ViewAction}.
     * This is used not to increment {@link #mBottomSheetIdlingResource} while the action is happening
     * since the view stops being idle during the action.
     */
    private boolean mSwipeActionInProgress;

    /**
     * @see com.github.zawadz88.sample.test.step.SampleListStepDefinitions#mBottomSheetIdlingResource
     */
    private CountingIdlingResource mBottomSheetIdlingResource;

    public IdlingResourceBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback wrappedCallback, CountingIdlingResource mBottomSheetIdlingResource) {
        this.mWrappedCallback = wrappedCallback;
        this.mBottomSheetIdlingResource = mBottomSheetIdlingResource;
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (!mSwipeActionInProgress) {
            if (newState == BottomSheetBehavior.STATE_SETTLING || newState == BottomSheetBehavior.STATE_DRAGGING) {
                if (mBottomSheetIdlingResource.isIdleNow()) {
                    mBottomSheetIdlingResource.increment();
                }
            } else if (!mBottomSheetIdlingResource.isIdleNow()) {
                mBottomSheetIdlingResource.decrement();
            }
        }
        mWrappedCallback.onStateChanged(bottomSheet, newState);
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
    }

    public void setSwipeActionInProgress(boolean swipeActionInProgress) {
        this.mSwipeActionInProgress = swipeActionInProgress;
    }
}