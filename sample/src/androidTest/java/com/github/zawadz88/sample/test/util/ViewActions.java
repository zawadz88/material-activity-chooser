package com.github.zawadz88.sample.test.util;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.Tap;

import com.github.zawadz88.sample.test.viewaction.NoConstraintsSwipeAction;

/**
 * @author Piotr Zawadzki
 */
public class ViewActions {

    public static ViewAction clickOnTop() {
        return new GeneralClickAction(Tap.SINGLE, GeneralLocation.TOP_CENTER, Press.FINGER);
    }

    public static ViewAction swipeBottomSheetDown() {
        /* The default swipe action has a constraint where the swiped view has to be displayed in at least 90%,
         * which is not the case with bottom sheets...
         */
        return new NoConstraintsSwipeAction(Swipe.FAST, GeneralLocation.TOP_CENTER,
                GeneralLocation. BOTTOM_CENTER, Press.FINGER);
    }

    public static ViewAction swipeBottomSheetUp() {
        /* The default swipe action has a constraint where the swiped view has to be displayed in at least 90%,
         * which is not the case with bottom sheets...
         */
        return new NoConstraintsSwipeAction(Swipe.FAST, GeneralLocation.VISIBLE_CENTER,
                GeneralLocation. TOP_CENTER, Press.FINGER);
    }
}
