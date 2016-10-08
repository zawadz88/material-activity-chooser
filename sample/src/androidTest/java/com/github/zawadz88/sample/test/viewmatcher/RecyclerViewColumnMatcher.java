package com.github.zawadz88.sample.test.viewmatcher;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * A view matcher checking if the recycler view has a correct number of columns.
 *
 * @author Piotr Zawadzki
 */
public class RecyclerViewColumnMatcher extends TypeSafeMatcher<View> {

    private int columnCount;

    public RecyclerViewColumnMatcher(int columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    protected boolean matchesSafely(View item) {
        if (item instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) item;
            if (recyclerView.getLayoutManager() != null && recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (gridLayoutManager.getSpanCount() == columnCount) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("columns: ").appendValue(columnCount);
    }
}
