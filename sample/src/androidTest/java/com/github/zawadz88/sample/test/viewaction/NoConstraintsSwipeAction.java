/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.zawadz88.sample.test.viewaction;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.PrecisionDescriber;
import android.support.test.espresso.action.Swiper;
import android.support.test.espresso.util.HumanReadables;
import android.view.View;
import android.view.ViewConfiguration;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Enables swiping across a view without the constraint from {@link android.support.test.espresso.action.GeneralSwipeAction}.
 */
public final class NoConstraintsSwipeAction implements ViewAction {

  /** Maximum number of times to attempt sending a swipe action. */
  private static final int MAX_TRIES = 3;

  private final CoordinatesProvider startCoordinatesProvider;
  private final CoordinatesProvider endCoordinatesProvider;
  private final Swiper swiper;
  private final PrecisionDescriber precisionDescriber;

  public NoConstraintsSwipeAction(Swiper swiper, CoordinatesProvider startCoordinatesProvider,
                                  CoordinatesProvider endCoordinatesProvider, PrecisionDescriber precisionDescriber) {
    this.swiper = swiper;
    this.startCoordinatesProvider = startCoordinatesProvider;
    this.endCoordinatesProvider = endCoordinatesProvider;
    this.precisionDescriber = precisionDescriber;
  }

  @Override
  public Matcher<View> getConstraints() {
    return isDisplayed();
  }

  @Override
  public void perform(UiController uiController, View view) {
    float[] startCoordinates = startCoordinatesProvider.calculateCoordinates(view);
    float[] endCoordinates = endCoordinatesProvider.calculateCoordinates(view);
    float[] precision = precisionDescriber.describePrecision();

    Swiper.Status status = Swiper.Status.FAILURE;

    for (int tries = 0; tries < MAX_TRIES && status != Swiper.Status.SUCCESS; tries++) {
      try {
        status = swiper.sendSwipe(uiController, startCoordinates, endCoordinates, precision);
      } catch (RuntimeException re) {
        throw new PerformException.Builder()
            .withActionDescription(this.getDescription())
            .withViewDescription(HumanReadables.describe(view))
            .withCause(re)
            .build();
      }

      int duration = ViewConfiguration.getPressedStateDuration();
      // ensures that all work enqueued to process the swipe has been run.
      if (duration > 0) {
        uiController.loopMainThreadForAtLeast(duration);
      }
    }

    if (status == Swiper.Status.FAILURE) {
      throw new PerformException.Builder()
          .withActionDescription(getDescription())
          .withViewDescription(HumanReadables.describe(view))
          .withCause(new RuntimeException(String.format(
              "Couldn't swipe from: %s,%s to: %s,%s precision: %s, %s . Swiper: %s "
              + "start coordinate provider: %s precision describer: %s. Tried %s times",
              startCoordinates[0],
              startCoordinates[1],
              endCoordinates[0],
              endCoordinates[1],
              precision[0],
              precision[1],
              swiper,
              startCoordinatesProvider,
              precisionDescriber,
              MAX_TRIES)))
          .build();
    }
  }

  @Override
  public String getDescription() {
    return swiper.toString().toLowerCase() + " swipe";
  }
}
