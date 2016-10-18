package com.github.zawadz88.activitychooser;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Piotr Zawadzki
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
public class MaterialActivityChooserBuilderTest {

    private static final String SOME_INTENT_ACTION = "some_intent_action";

    private static final String SOME_OTHER_INTENT_ACTION = "some_other_intent_action";

    private static final String SOME_PACKAGE = "some package";

    @Mock
    Context mockContext;

    @Mock
    Intent mockIntent;

    @Mock
    Intent mockSecondaryIntent;

    @Captor
    ArgumentCaptor<Intent> intentArgumentCaptor;

    MaterialActivityChooserBuilder builder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        builder = new MaterialActivityChooserBuilder(mockContext);
    }

    @Test
    public void should_not_start_activity_if_intent_not_provided() {
        //when
        builder.show();

        //then
        verifyZeroInteractions(mockContext);
    }

    @Test
    public void should_not_start_activity_if_intent_action_not_provided() {
        //given
        builder.withIntent(mockIntent);

        //when
        builder.show();

        //then
        verifyZeroInteractions(mockContext);
    }
    @Test
    public void should_not_pass_secondary_intents_to_activity_if_packages_array_is_empty() {
        //given
        doReturn(SOME_INTENT_ACTION).when(mockIntent).getAction();
        builder
                .withIntent(mockIntent)
                .withSecondaryIntent(mockSecondaryIntent);

        //when
        builder.show();

        //then
        verifyActivityStarted();
        assertThatSecondaryIntentsWereNotPassedToActivity();
    }

    @Test
    public void should_not_pass_secondary_intents_to_activity_if_packages_array_contains_empty_strings_only() {
        //given
        doReturn(SOME_INTENT_ACTION).when(mockIntent).getAction();
        builder
                .withIntent(mockIntent)
                .withSecondaryIntent(mockSecondaryIntent, "");

        //when
        builder.show();

        //then
        verifyActivityStarted();
        assertThatSecondaryIntentsWereNotPassedToActivity();
    }

    @Test
    public void should_not_pass_secondary_intents_to_activity_if_secondary_intent_has_different_action() {
        //given
        doReturn(SOME_INTENT_ACTION).when(mockIntent).getAction();
        doReturn(SOME_OTHER_INTENT_ACTION).when(mockSecondaryIntent).getAction();
        builder
                .withIntent(mockIntent)
                .withSecondaryIntent(mockSecondaryIntent, SOME_PACKAGE);

        //when
        builder.show();

        //then
        verifyActivityStarted();
        assertThatSecondaryIntentsWereNotPassedToActivity();
    }

    @Test
    public void should_pass_secondary_intents_to_activity_if_packages_array_contains_non_empty_strings() {
        //given
        doReturn(SOME_INTENT_ACTION).when(mockIntent).getAction();
        doReturn(SOME_INTENT_ACTION).when(mockSecondaryIntent).getAction();
        builder
                .withIntent(mockIntent)
                .withSecondaryIntent(mockSecondaryIntent, SOME_PACKAGE);

        //when
        builder.show();

        //then
        verifyActivityStarted();
        assertTrue("Should contain the secondary intents extra", intentArgumentCaptor.getValue().hasExtra(MaterialActivityChooserActivity.SECONDARY_INTENTS_KEY));
    }

    private void assertThatSecondaryIntentsWereNotPassedToActivity() {
        assertFalse("Should not contain the secondary intents extra", intentArgumentCaptor.getValue().hasExtra(MaterialActivityChooserActivity.SECONDARY_INTENTS_KEY));
    }

    private void verifyActivityStarted() {
        verify(mockContext).startActivity(intentArgumentCaptor.capture());
    }
}