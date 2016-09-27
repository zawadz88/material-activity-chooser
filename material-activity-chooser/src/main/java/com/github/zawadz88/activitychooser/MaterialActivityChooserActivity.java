package com.github.zawadz88.activitychooser;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.github.zawadz88.activitychooser.adapter.ActivityAdapter;
import com.github.zawadz88.activitychooser.util.AttributeHelper;
import com.github.zawadz88.activitychooser.util.IntentResolver;
import com.github.zawadz88.activitychooser.widget.BottomSheetTitleTextView;

import java.util.HashMap;
import java.util.List;

/**
 * Displays a bottom sheet with a list of resolved activities or an empty view when none were found.
 *
 * @author Piotr Zawadzki
 */
public class MaterialActivityChooserActivity extends AppCompatActivity implements ActivityAdapter.OnActivityInteractionListener {

    public static final String INTENT_KEY = "intent";
    public static final String TITLE_KEY = "title";
    public static final String TITLE_RESOURCE_ID_KEY = "titleResourceId";
    public static final String EMPTY_VIEW_TITLE_KEY = "emptyViewTitle";
    public static final String EMPTY_VIEW_TITLE_RESOURCE_ID_KEY = "emptyViewTitleResourceId";
    public static final String EMPTY_VIEW_BUTTON_TITLE_KEY = "emptyViewButtonTitle";
    public static final String EMPTY_VIEW_BUTTON_TITLE_RESOURCE_ID_KEY = "emptyViewButtonTitleResourceId";
    public static final String EMPTY_VIEW_ACTION_KEY = "emptyViewAction";
    public static final String EMPTY_VIEW_LAYOUT_KEY = "emptyViewLayout";
    public static final String SECONDARY_INTENTS_KEY = "secondaryIntentsKey";

    private static final String TAG = "MatActChooserActivity";

    private static final int CONTENT_FADE_IN_DURATION = 300;
    private static final long BOTTOM_SHEET_ENTER_ANIMATION_OFFSET = 200L;

    private View mBottomSheet;

    private RecyclerView mRecyclerView;

    private BottomSheetTitleTextView mTitleView;

    private View mContentView;

    private View mEmptyView;

    private TextView mEmptyViewTitleTextView;

    private Button mEmptyViewButton;

    private ViewStub mEmptyViewStub;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private IntentResolver mIntentResolver;

    private Intent mIntent;

    private HashMap<String, Intent> mSecondaryIntents;

    private PendingIntent mActionPendingIntent;

    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_activity_chooser);

        mIntent = getIntent().getParcelableExtra(INTENT_KEY);
        //noinspection unchecked
        mSecondaryIntents = (HashMap<String, Intent>) getIntent().getSerializableExtra(SECONDARY_INTENTS_KEY);

        mIntentResolver = new IntentResolver(this);

        findViews();

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        initTitle();

        initBottomSheet();

        initEmptyView();

        fadeContentIn();
    }

    private void findViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mac_recycler_view);
        mBottomSheet = findViewById(R.id.mac_bottom_sheet);
        mContentView = findViewById(R.id.mac_content_view);
        mTitleView = (BottomSheetTitleTextView) findViewById(R.id.mac_title);
        mEmptyViewStub = (ViewStub) findViewById(R.id.mac_empty_view_custom_view_stub);
        mEmptyView = findViewById(R.id.mac_empty_view);
        mEmptyViewTitleTextView = (TextView) findViewById(R.id.mac_empty_view_title);
        mEmptyViewButton = (Button) findViewById(R.id.mac_empty_view_button);
    }

    private void initTitle() {
        if (getIntent().hasExtra(TITLE_KEY)) {
            mTitleView.setText(getIntent().getStringExtra(TITLE_KEY));
        } else {
            mTitleView.setText(getIntent().getIntExtra(TITLE_RESOURCE_ID_KEY, R.string.mac_default_title));
        }
    }

    private void initEmptyView() {
        if (getIntent().hasExtra(EMPTY_VIEW_LAYOUT_KEY)) {
            mEmptyViewStub.setLayoutResource(getIntent().getIntExtra(EMPTY_VIEW_LAYOUT_KEY, 0));
            mEmptyViewStub.setVisibility(View.VISIBLE);
            mEmptyViewTitleTextView.setVisibility(View.GONE);
            mEmptyViewButton.setVisibility(View.GONE);
            return;
        }
        mEmptyViewTitleTextView.setVisibility(View.VISIBLE);
        if (getIntent().hasExtra(EMPTY_VIEW_TITLE_KEY)) {
            mEmptyViewTitleTextView.setText(getIntent().getStringExtra(EMPTY_VIEW_TITLE_KEY));
        } else {
            mEmptyViewTitleTextView.setText(getIntent().getIntExtra(EMPTY_VIEW_TITLE_RESOURCE_ID_KEY, R.string.mac_default_no_activities_found_message));
        }

        if (getIntent().hasExtra(EMPTY_VIEW_ACTION_KEY)) {
            mActionPendingIntent = getIntent().getParcelableExtra(EMPTY_VIEW_ACTION_KEY);
            mEmptyViewButton.setVisibility(View.VISIBLE);
            if (getIntent().hasExtra(EMPTY_VIEW_BUTTON_TITLE_KEY)) {
                mEmptyViewButton.setText(getIntent().getStringExtra(EMPTY_VIEW_BUTTON_TITLE_KEY));
            } else {
                mEmptyViewButton.setText(getIntent().getIntExtra(EMPTY_VIEW_BUTTON_TITLE_RESOURCE_ID_KEY, R.string.mac_default_empty_view_button_message));
            }
            mEmptyViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mActionPendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        Log.w(TAG, "Could not fire pending intent.", e);
                    }
                }
            });
        } else {
            mEmptyViewButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void initBottomSheet() {
        int spanCount = AttributeHelper.getIntegerFromAttribute(this, R.attr.mac_activityItemSpanCount, getResources().getInteger(R.integer.mac_span_count));

        mLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<ResolveInfo> resolvedActivities = mIntentResolver.findActivities(mIntent);

        if (resolvedActivities.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(new ActivityAdapter(this, resolvedActivities, getPackageManager()));
        }

        initScrollListener();

        initBehavior();
    }

    private void initScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstCompletelyVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                mTitleView.setTitleOverContent(firstCompletelyVisibleItemPosition != 0);
            }
        });
    }

    private void initBehavior() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    finish();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }, BOTTOM_SHEET_ENTER_ANIMATION_OFFSET);
    }

    private void fadeContentIn() {
        mContentView.setAlpha(0);
        mContentView.animate()
                .alpha(1)
                .setDuration(CONTENT_FADE_IN_DURATION)
                .start();
    }

    @Override
    public void onActivityClicked(ResolveInfo activity) {
        String packageName = activity.activityInfo.packageName;
        ComponentName chosenName = new ComponentName(
                packageName,
                activity.activityInfo.name);

        Intent choiceIntent;
        if (mSecondaryIntents != null && mSecondaryIntents.containsKey(packageName)) {
            choiceIntent = new Intent(mSecondaryIntents.get(packageName));
        } else {
            choiceIntent = new Intent(mIntent);
        }

        choiceIntent.setComponent(chosenName);
        startActivity(choiceIntent);
        finish();
    }

    @Override
    public boolean onActivityLongClicked(ResolveInfo activity) {
        String packageName = activity.activityInfo.packageName;

        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);

        return true;
    }
}
