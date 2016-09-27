package com.github.zawadz88.activitychooser.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.github.zawadz88.activitychooser.R;

/**
 * An {@link AppCompatTextView} with an extra state {@link #STATE_OVER_CONTENT}
 * used for changing title elevation/background when scrolling the list of activities.
 *
 * @author Piotr Zawadzki
 */
public class BottomSheetTitleTextView extends AppCompatTextView {

    private static final int[] STATE_OVER_CONTENT = {R.attr.state_over_content};

    /**
     * Flag indicating if the bottom sheet's title is currently above the list of activities.
     * This happens when the list items are scrolled and hide below it.
     */
    private boolean mTitleOverContent = false;

    public BottomSheetTitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BottomSheetTitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomSheetTitleTextView(Context context) {
        super(context);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mTitleOverContent) {
            mergeDrawableStates(drawableState, STATE_OVER_CONTENT);
        }
        return drawableState;
    }

    /**
     * @see #mTitleOverContent
     */
    public boolean isTitleOverContent() {
        return mTitleOverContent;
    }

    /**
     * @see #mTitleOverContent
     */
    public void setTitleOverContent(boolean titleOverContent) {
        this.mTitleOverContent = titleOverContent;
        refreshDrawableState();
    }
}
