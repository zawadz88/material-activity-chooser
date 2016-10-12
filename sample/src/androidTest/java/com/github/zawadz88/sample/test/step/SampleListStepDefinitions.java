package com.github.zawadz88.sample.test.step;

import android.app.Instrumentation;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.intercepting.SingleActivityFactory;
import android.support.v7.widget.AppCompatImageView;

import com.github.zawadz88.activitychooser.MaterialActivityChooserActivity;
import com.github.zawadz88.sample.R;
import com.github.zawadz88.sample.SampleListActivity;
import com.github.zawadz88.sample.test.rule.IntentTestRuleWithActivityFactory;
import com.github.zawadz88.sample.test.util.ActivityUtils;
import com.github.zawadz88.sample.test.util.IdlingResourceBottomSheetCallback;

import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import cucumber.api.CucumberOptions;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.jodamob.reflect.SuperReflect;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasPackageName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasFlags;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.zawadz88.sample.test.matcher.StringUriMatcher.withStringUri;
import static com.github.zawadz88.sample.test.util.ViewActions.clickOnTop;
import static com.github.zawadz88.sample.test.util.ViewActions.swipeBottomSheetDown;
import static com.github.zawadz88.sample.test.util.ViewActions.swipeBottomSheetUp;
import static com.github.zawadz88.sample.test.util.ViewMatchers.withColumnCount;
import static com.github.zawadz88.sample.test.util.ViewMatchers.withRecyclerView;
import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

/**
 * @author Piotr Zawadzki
 */
@CucumberOptions(features = "features/samples.feature", // Test scenarios
        glue = {"com.github.zawadz88.sample.test.step"}, // Steps definitions
        format = {"pretty", "html:/sdcard/activity-chooser-cucumber-report"}
)
@RunWith(AndroidJUnit4.class)
public class SampleListStepDefinitions {

    private static final int TEST_REQUEST_CODE = 123;

    private static final String DUMMY_ACTION = "DUMMY_ACTION";

    @Rule
    public IntentTestRuleWithActivityFactory<SampleListActivity> mActivityRule = new IntentTestRuleWithActivityFactory<>(getActivityFactory(),
            true,
            false);

    private CountingIdlingResource mBottomSheetIdlingResource = new CountingIdlingResource("bottomSheet");

    private IdlingResourceBottomSheetCallback mIdlingResourceBottomSheetCallback;

    private PendingIntent mStubbedEmptyViewActionPendingIntent;

    private AtomicBoolean mTestBroadcastReceived = new AtomicBoolean(false);

    private BroadcastReceiver mTestBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTestBroadcastReceived.set(true);
        }
    };

    @Before
    public void before() {
        Espresso.registerIdlingResources(mBottomSheetIdlingResource);
        Intent dummyIntent = new Intent(DUMMY_ACTION);
        mStubbedEmptyViewActionPendingIntent = PendingIntent.getBroadcast(InstrumentationRegistry.getTargetContext(), TEST_REQUEST_CODE, dummyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @After
    public void after() {
        Espresso.unregisterIdlingResources(mBottomSheetIdlingResource);
        try {
            InstrumentationRegistry.getTargetContext().unregisterReceiver(mTestBroadcastReceiver);
        } catch (IllegalArgumentException ignored){}
    }

    @SuppressWarnings("UnusedParameters")
    @After
    public static void after(Scenario scenario) {
        ActivityUtils.finishOpenActivities();
        Intents.release();
    }

    private SingleActivityFactory<SampleListActivity> getActivityFactory() {
        return new SingleActivityFactory<SampleListActivity>(SampleListActivity.class) {
            @Override
            protected SampleListActivity create(Intent intent) {
                return new SampleListActivity() {
                    @NonNull
                    @Override
                    protected PendingIntent getActionPendingIntent() {
                        //we change the pending intent that gets created to a custom one so that we can actually check if it gets sent
                        return mStubbedEmptyViewActionPendingIntent;
                    }
                };
            }
        };
    }

    @Given("^I see samples list$")
    public void i_see_samples_list() {
        mActivityRule.launchActivity(null);
        stubExternalComponents();
    }

    @Given("^I see an activity chooser with items$")
    public void i_see_an_activity_chooser_with_items() {
        i_tap_on_button("Share text");
        waitUntilBottomSheetDisplayed();
    }

    @Given("^I see an activity chooser with '(.+)' items$")
    public void i_see_an_activity_chooser_with_PDF_reader_items(String buttonText) {
        i_tap_on_button(buttonText);
        waitUntilBottomSheetDisplayed();
    }

    @When("^I tap on the '(.+)' button$")
    public void i_tap_on_button(String buttonText) {
        onView(withText(buttonText)).perform(scrollTo(), click());
    }

    @When("^I tap the Back button$")
    public void i_tap_the_back_button() {
        Espresso.pressBack();
    }

    @When("^I tap the content view$")
    public void i_tap_the_content_view() {
        onView(withId(R.id.mac_content_view)).perform(clickOnTop());
    }

    @When("^I swipe the bottom sheet down$")
    public void i_swipe_the_bottom_sheet_down() {
        mIdlingResourceBottomSheetCallback.setSwipeActionInProgress(true);
        onView(withId(R.id.mac_bottom_sheet)).perform(swipeBottomSheetDown());
    }

    @When("^I tap on an activity item$")
    public void i_tap_on_an_activity_item() {
        onView(withRecyclerView(R.id.mac_recycler_view).atPosition(0))
                .perform(click());
    }

    @When("^I long-tap on an activity item$")
    public void i_long_tap_on_an_activity_item() {
        onView(withRecyclerView(R.id.mac_recycler_view).atPosition(0))
                .perform(longClick());
    }

    @When("^I tap on '(.+)' activity item$")
    public void i_tap_on_a_selected_activity_item(String appName) {
        mIdlingResourceBottomSheetCallback.setSwipeActionInProgress(true);
        // TODO: 09/10/2016 this is a naive implementation, if there are many items then this test could fail
        onView(withId(R.id.mac_bottom_sheet)).perform(swipeBottomSheetUp());
        onView(withId(R.id.mac_recycler_view)).perform(actionOnItem(withChild(withText(appName)), click()));
    }

    @When("^I tap the empty view action button$")
    public void i_tap_the_empty_view_action_button() {
        InstrumentationRegistry.getTargetContext().registerReceiver(mTestBroadcastReceiver, new IntentFilter(DUMMY_ACTION));
        onView(withId(R.id.mac_empty_view_button)).perform(click());
    }

    @Then("^I should see a system activity chooser$")
    public void i_should_see_a_system_activity_chooser() {
        intended(
                allOf(
                        hasAction(Intent.ACTION_CHOOSER),
                        hasExtra(Intent.EXTRA_TITLE, "Share via")));
    }

    @Then("^activity chooser should be dismissed$")
    public void activity_chooser_should_be_dismissed() {
        await().until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !(ActivityUtils.getCurrentActivity(mActivityRule) instanceof MaterialActivityChooserActivity);
            }
        });
        onView(withId(R.id.mac_bottom_sheet)).check(doesNotExist());
    }

    @Then("^I should see an activity chooser with text '(.+)'$")
    public void i_should_see_an_activity_chooser_with_text(String chooserTitle) {
        waitUntilBottomSheetDisplayed();
        onView(withId(R.id.mac_title)).check(matches(allOf(isCompletelyDisplayed(), withText(chooserTitle))));
    }

    @Then("^I should see at least one activity item$")
    public void i_should_see_at_least_one_activity_item() {
        onView(withId(R.id.mac_recycler_view))
                .check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.mac_recycler_view).atPosition(0))
                .check(matches(allOf(
                        isDisplayed(),
                        withChild(withId(R.id.mac_item_activity_icon)),
                        withChild(withId(R.id.mac_item_activity_label)))));
    }

    @Then("^I should not see an empty view$")
    public void i_should_not_see_an_empty_view() {
        onView(withId(R.id.mac_empty_view)).check(matches(not(isDisplayed())));
    }

    @Then("^I should see the clicked activity$")
    public void i_should_see_the_clicked_activity() {
        intended(
                allOf(
                        hasAction(Intent.ACTION_SEND),
                        hasType(SampleListActivity.TEXT_PLAIN_TYPE),
                        hasExtra(Intent.EXTRA_TEXT, SampleListActivity.DEFAULT_EXTRA_TEXT),
                        hasExtra(Intent.EXTRA_TITLE, SampleListActivity.DEFAULT_EXTRA_TITLE),
                        hasComponent(notNullValue(ComponentName.class))));
    }

    @Then("^I should see the clicked 'PDF reader' activity$")
    public void i_should_see_the_clicked_PDF_reader_activity() {
        intended(
                allOf(
                        hasAction(Intent.ACTION_VIEW),
                        hasType(SampleListActivity.MIME_TYPE_APPLICATION_PDF),
                        hasData("content://com.github.zawadz88.sample.FILE_PROVIDER/my_files/Latin-Lipsum.pdf"),
                        hasFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_GRANT_READ_URI_PERMISSION),
                        hasComponent(notNullValue(ComponentName.class))));
    }

    @Then("^I should see the clicked activity's settings$")
    public void i_should_see_the_clicked_activity_settings() {
        intended(
                allOf(
                        hasAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS),
                        hasData(withStringUri(startsWith("package:")))));
    }

    @Then("^I should see the clicked activity with package name: '(.+)', extra title: '(.+)' and extra text '(.+)'$")
    public void i_should_see_the_clicked_activity_with_fields(String packageName, String extraTitle, String extraText) {
        intended(
                allOf(
                        hasAction(Intent.ACTION_SEND),
                        hasType(SampleListActivity.TEXT_PLAIN_TYPE),
                        hasExtra(Intent.EXTRA_TEXT, extraText),
                        hasExtra(Intent.EXTRA_TITLE, extraTitle),
                        hasComponent(hasPackageName(packageName))));
    }

    @Then("^I should see an empty view$")
    public void i_should_see_an_empty_view() {
        onView(withId(R.id.mac_empty_view)).check(matches(isDisplayed()));
    }

    @Then("^I should see empty view message '(.+)'$")
    public void i_should_see_empty_view_message(String emptyViewMessage) {
        onView(withId(R.id.mac_empty_view_title)).check(matches(withText(emptyViewMessage)));
    }

    @Then("^I should not see an action button in empty view$")
    public void i_should_not_see_an_action_button_in_empty_view() {
        onView(withId(R.id.mac_empty_view_button)).check(matches(not(isDisplayed())));
    }

    @Then("^I should see an action button in empty view with text '(.+)'$")
    public void i_should_see_an_action_button_in_empty_view_with_text(String buttonText) {
        onView(withId(R.id.mac_empty_view_button)).check(matches(withText(buttonText)));
    }

    @Then("^I should not see an empty view message$")
    public void i_should_not_see_an_empty_view_message() {
        onView(withId(R.id.mac_empty_view_title)).check(matches(not(isDisplayed())));
    }

    @Then("^I should see a custom empty view layout$")
    public void i_should_see_a_custom_empty_view_layout() {
        onView(
                allOf(
                        withParent(withId(R.id.mac_empty_view)),
                        withClassName(is(AppCompatImageView.class.getName()))
                )
        ).check(matches(isDisplayed()));
    }

    @Then("^I should see a list with (\\d+) columns$")
    public void i_should_see_a_list_with_columns(int columnCount) {
        onView(withId(R.id.mac_recycler_view)).check(matches(withColumnCount(columnCount)));
    }

    @Then("^I should see a tracking message$")
    public void i_should_see_a_tracking_message() {
        onView(withText(startsWith("Application clicked: ")))
                .inRoot(withDecorView(not(is(ActivityUtils.getCurrentActivity(mActivityRule).getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Then("^I should see the action result$")
    public void i_should_see_the_action_result() {
        await().atMost(3, TimeUnit.SECONDS).untilTrue(mTestBroadcastReceived);
    }

    private void waitUntilBottomSheetDisplayed() {
        MaterialActivityChooserActivity activityChooserActivity = ActivityUtils.getCurrentActivity(mActivityRule);
        BottomSheetBehavior.BottomSheetCallback originalBottomSheetCallback = SuperReflect.on(activityChooserActivity).get("mBottomSheetCallback");
        if (!(originalBottomSheetCallback instanceof IdlingResourceBottomSheetCallback)) {
            final BottomSheetBehavior bottomSheetBehavior = SuperReflect.on(activityChooserActivity)
                    .get("mBottomSheetBehavior");

            /* The bottom sheet gets opened with a delay (using Handler#postDelayed())
             * therefore we need to initially wait for the bottom sheet to get to the 'collapsed' state
             */
            await().until(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED;
                }
            });
            mIdlingResourceBottomSheetCallback = new IdlingResourceBottomSheetCallback(originalBottomSheetCallback, mBottomSheetIdlingResource);
            bottomSheetBehavior.setBottomSheetCallback(mIdlingResourceBottomSheetCallback);
        }
        onView(withId(R.id.mac_bottom_sheet)).check(matches(isDisplayed()));
    }

    private void stubExternalComponents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(0, null));
    }
}