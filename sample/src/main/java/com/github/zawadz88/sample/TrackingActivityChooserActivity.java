package com.github.zawadz88.sample;

import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.github.zawadz88.activitychooser.MaterialActivityChooserActivity;

public class TrackingActivityChooserActivity extends MaterialActivityChooserActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityClicked(ResolveInfo activity) {
        Toast.makeText(this, "Application clicked: " + activity.activityInfo.packageName, Toast.LENGTH_SHORT).show();
        super.onActivityClicked(activity);
    }
}
