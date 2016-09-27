package com.github.zawadz88.activitychooser.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Finds activities which can handle the provided intent.
 *
 * @author Piotr Zawadzki
 */
public class IntentResolver {

    private Context mContext;

    public IntentResolver(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Returns a list of activities which can handle the given intent.
     * @param intent intent to resolve
     * @return a list of activities
     * @see android.content.pm.PackageManager#queryIntentActivities(Intent, int)
     */
    public List<ResolveInfo> findActivities(Intent intent) {
        return mContext.getPackageManager().queryIntentActivities(intent, 0);
    }
}
