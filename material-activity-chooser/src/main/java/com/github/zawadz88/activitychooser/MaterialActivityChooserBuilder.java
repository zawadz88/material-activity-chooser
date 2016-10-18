package com.github.zawadz88.activitychooser;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * A builder class responsible for creating a {@link MaterialActivityChooserActivity}.
 * You must call {@link #show()} at the end to actually start the activity.
 * {@link #withIntent(Intent)} is required to launch the activity, the rest is optional.
 *
 * @author Piotr Zawadzki
 */
public final class MaterialActivityChooserBuilder {

    private static final String TAG = "MatActChooserBuilder";

    @NonNull
    private final Context mContext;

    private Intent mIntent;

    private String mTitle;

    @StringRes
    private int mTitleResourceId;

    private String mEmptyViewTitle;

    @StringRes
    private int mEmptyViewTitleResourceId;

    @NonNull
    private HashMap<String, Intent> secondaryIntentsForPackages = new HashMap<>();

    @NonNull
    private Class<? extends MaterialActivityChooserActivity> mActivityClass = MaterialActivityChooserActivity.class;

    private int mEmptyViewButtonTextResourceId;

    private String mEmptyViewButtonText;

    private PendingIntent mActionPendingIntent;

    @LayoutRes
    private int mCustomEmptyViewLayoutResourceId;

    public MaterialActivityChooserBuilder(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Use a custom share activity.
     * @param activityClass activity extending {@link MaterialActivityChooserActivity}
     * @return self
     */
    public MaterialActivityChooserBuilder withActivity(@NonNull Class<? extends MaterialActivityChooserActivity> activityClass) {
        this.mActivityClass = activityClass;
        return this;
    }

    /**
     * The intent for which to find the activities and send to these activity once an activity is selected.
     * @param intent intent to use
     * @return self
     */
    public MaterialActivityChooserBuilder withIntent(@NonNull Intent intent) {
        mIntent = intent;
        return this;
    }

    /**
     * Sets a custom bottom sheet title.
     * @param title bottom sheet title
     * @return self
     */
    public MaterialActivityChooserBuilder withTitle(@NonNull String title) {
        mTitle = title;
        return this;
    }

    /**
     * Sets a custom bottom sheet title.
     * @param titleResourceId bottom sheet title resource ID
     * @return self
     */
    public MaterialActivityChooserBuilder withTitle(@StringRes int titleResourceId) {
        mTitleResourceId = titleResourceId;
        return this;
    }

    /**
     * Sets a custom view to be inflated if there were no activities found.
     * @param customViewLayoutResourceId layout resource ID of the custom view
     * @return self
     */
    public MaterialActivityChooserBuilder withEmptyViewCustomView(@LayoutRes int customViewLayoutResourceId) {
        mCustomEmptyViewLayoutResourceId = customViewLayoutResourceId;
        return this;
    }

    /**
     * Sets a custom message if there were no activities found.
     * @param emptyViewTitle title of the empty view
     * @return self
     */
    public MaterialActivityChooserBuilder withEmptyViewTitle(@NonNull String emptyViewTitle) {
        mEmptyViewTitle = emptyViewTitle;
        return this;
    }

    /**
     * Sets a custom message if there were no activities found.
     * @param emptyViewTitleResourceId resource ID of title of the empty view
     * @return self
     */
    public MaterialActivityChooserBuilder withEmptyViewTitle(@StringRes int emptyViewTitleResourceId) {
        mEmptyViewTitleResourceId = emptyViewTitleResourceId;
        return this;
    }

    /**
     * Adds an action to be fired once the user clicks a button if there were no activities found.
     * Providing an {@link #mActionPendingIntent} is required to show the button.
     * @param actionPendingIntent pending intent to be fired once the button is clicked
     * @return self
     */
    public MaterialActivityChooserBuilder withEmptyViewAction(@NonNull PendingIntent actionPendingIntent) {
        mActionPendingIntent = actionPendingIntent;
        return this;
    }

    /**
     * Adds an action to be fired once the user clicks a button if there were no activities found.
     * Providing an {@link #mActionPendingIntent} is required to show the button.
     * This also sets a custom button text.
     * @param emptyViewButtonText text of the button
     * @param actionPendingIntent pending intent to be fired once the button is clicked
     * @return self
     */
    public MaterialActivityChooserBuilder withEmptyViewAction(@NonNull String emptyViewButtonText, @NonNull PendingIntent actionPendingIntent) {
        mEmptyViewButtonText = emptyViewButtonText;
        return withEmptyViewAction(actionPendingIntent);
    }

    /**
     * Adds an action to be fired once the user clicks a button if there were no activities found.
     * Providing an {@link #mActionPendingIntent} is required to show the button.
     * This also sets a custom button text from a resource ID.
     * @param emptyViewButtonTextResourceId text of the button
     * @param actionPendingIntent pending intent to be fired once the button is clicked
     * @return self
     */
    public MaterialActivityChooserBuilder withEmptyViewAction(@StringRes int emptyViewButtonTextResourceId, @NonNull PendingIntent actionPendingIntent) {
        mEmptyViewButtonTextResourceId = emptyViewButtonTextResourceId;
        return withEmptyViewAction(actionPendingIntent);
    }

    /**
     * Sets an alternative intent to be used for an array of activities identified by their package names.
     * @param secondaryIntent an alternative intent to pass to selected activities when they're selected, <b>it must be compatible with the main intent i.e. have at least the same ACTION</b>
     * @param packageNames an array of package names for which to use a secondary intent
     * @return self
     */
    public MaterialActivityChooserBuilder withSecondaryIntent(@NonNull Intent secondaryIntent, @NonNull String ... packageNames) {
        if (packageNames.length > 0) {
            for (String packageName : packageNames) {
                if (!TextUtils.isEmpty(packageName)) {
                    secondaryIntentsForPackages.put(packageName, secondaryIntent);
                }
            }
        }
        return this;
    }

    /**
     * Shows the bottom sheet activity configured with the data provided.
     */
    public void show() {
        if (mIntent == null || mIntent.getAction() == null) {
            Log.e(TAG, "Cannot show the share screen - intent is missing or has no action.");
            return;
        }
        Intent materialShareIntent = new Intent(mContext, mActivityClass);
        materialShareIntent.putExtra(MaterialActivityChooserActivity.INTENT_KEY, mIntent);
        if (mTitle != null) {
            materialShareIntent.putExtra(MaterialActivityChooserActivity.TITLE_KEY, mTitle);
        }
        if (mTitleResourceId != 0) {
            materialShareIntent.putExtra(MaterialActivityChooserActivity.TITLE_RESOURCE_ID_KEY, mTitleResourceId);
        }
        if (mCustomEmptyViewLayoutResourceId != 0) {
            materialShareIntent.putExtra(MaterialActivityChooserActivity.EMPTY_VIEW_LAYOUT_KEY, mCustomEmptyViewLayoutResourceId);
        }
        if (mEmptyViewTitle != null) {
            materialShareIntent.putExtra(MaterialActivityChooserActivity.EMPTY_VIEW_TITLE_KEY, mEmptyViewTitle);
        }
        if (mEmptyViewTitleResourceId != 0) {
            materialShareIntent.putExtra(MaterialActivityChooserActivity.EMPTY_VIEW_TITLE_RESOURCE_ID_KEY, mEmptyViewTitleResourceId);
        }
        if (mEmptyViewButtonText != null) {
            materialShareIntent.putExtra(MaterialActivityChooserActivity.EMPTY_VIEW_BUTTON_TITLE_KEY, mEmptyViewButtonText);
        }
        if (mEmptyViewButtonTextResourceId != 0) {
            materialShareIntent.putExtra(MaterialActivityChooserActivity.EMPTY_VIEW_BUTTON_TITLE_RESOURCE_ID_KEY, mEmptyViewButtonTextResourceId);
        }
        if (mActionPendingIntent != null) {
            materialShareIntent.putExtra(MaterialActivityChooserActivity.EMPTY_VIEW_ACTION_KEY, mActionPendingIntent);
        }
        if (!secondaryIntentsForPackages.isEmpty()) {
            for (String packageName : secondaryIntentsForPackages.keySet()) {
                Intent secondaryIntent = secondaryIntentsForPackages.get(packageName);
                if (isIncompatibleIntent(secondaryIntent)) {
                    secondaryIntentsForPackages.remove(packageName);
                    Log.w(TAG, "Removing package '" + packageName + "' since its secondary intent is incompatible with the main intent!");
                }
            }
            if (!secondaryIntentsForPackages.isEmpty()) {
                materialShareIntent.putExtra(MaterialActivityChooserActivity.SECONDARY_INTENTS_KEY, secondaryIntentsForPackages);
            }

        }
        mContext.startActivity(materialShareIntent);
    }

    private boolean isIncompatibleIntent(Intent secondaryIntent) {
        return !mIntent.getAction().equals(secondaryIntent.getAction());
    }

}
