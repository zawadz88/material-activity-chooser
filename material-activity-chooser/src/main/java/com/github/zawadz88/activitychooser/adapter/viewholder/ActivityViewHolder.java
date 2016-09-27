package com.github.zawadz88.activitychooser.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.zawadz88.activitychooser.R;

/**
 * ViewHolder for the activity items.
 *
 * @author Piotr Zawadzki
 */
public class ActivityViewHolder extends RecyclerView.ViewHolder {

    public ImageView iconView;

    public TextView labelView;

    public static ActivityViewHolder newInstance(ViewGroup parent) {
        View  v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(v);
    }

    public ActivityViewHolder(View itemView) {
        super(itemView);
        iconView = (ImageView) itemView.findViewById(R.id.mac_item_activity_icon);
        labelView = (TextView) itemView.findViewById(R.id.mac_item_activity_label);
    }

}
