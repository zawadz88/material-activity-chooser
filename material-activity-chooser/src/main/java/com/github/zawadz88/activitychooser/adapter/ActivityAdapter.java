package com.github.zawadz88.activitychooser.adapter;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.zawadz88.activitychooser.adapter.viewholder.ActivityViewHolder;

import java.util.List;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} showing a list of activities.
 *
 * @author Piotr Zawadzki
 */
public class ActivityAdapter extends RecyclerView.Adapter<ActivityViewHolder> {

    @NonNull
    private final OnActivityInteractionListener mOnActivityInteractionListener;

    @NonNull
    private final List<ResolveInfo> mActivities;

    @NonNull
    private final PackageManager mPackageManager;

    public ActivityAdapter(@NonNull OnActivityInteractionListener onActivityInteractionListener,
                           @NonNull List<ResolveInfo> activities,
                           @NonNull PackageManager packageManager) {
        this.mOnActivityInteractionListener = onActivityInteractionListener;
        this.mActivities = activities;
        this.mPackageManager = packageManager;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ActivityViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        final ResolveInfo activity = mActivities.get(position);
        holder.iconView.setImageDrawable(activity.loadIcon(mPackageManager));
        holder.labelView.setText(activity.loadLabel(mPackageManager));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnActivityInteractionListener.onActivityClicked(activity);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mOnActivityInteractionListener.onActivityLongClicked(activity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActivities.size();
    }

    /**
     * A listener for interaction with the activity items.
     */
    public interface OnActivityInteractionListener {

        /**
         * Called when the activity item gets clicked
         * @param activity clicked activity
         */
        void onActivityClicked(ResolveInfo activity);

        /**
         * Called when the activity item gets long-clicked
         * @param activity long-clicked activity
         */
        boolean onActivityLongClicked(ResolveInfo activity);
    }
}
