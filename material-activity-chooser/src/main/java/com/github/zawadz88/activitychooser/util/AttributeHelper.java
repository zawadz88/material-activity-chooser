package com.github.zawadz88.activitychooser.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;

/**
 * A helper class for extracting theme attributes.
 *
 * @author Piotr Zawadzki
 */
public final class AttributeHelper {

    private AttributeHelper(){}

    /**
     * Gets an integer from an attribute or uses a default value if not present.
     * @param context context
     * @param attrResId resource ID of the attribute
     * @param defaultValue fallback value
     * @return integer
     */
    public static int getIntegerFromAttribute(Context context, @AttrRes int attrResId, int defaultValue) {
        int[] attrs = new int[] {attrResId};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        final int value = ta.getInteger(0, defaultValue);
        ta.recycle();

        return value;
    }
}
