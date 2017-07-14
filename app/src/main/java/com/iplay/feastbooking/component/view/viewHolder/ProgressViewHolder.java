package com.iplay.feastbooking.component.view.viewHolder;

import android.view.View;
import android.widget.ProgressBar;

import com.iplay.feastbooking.R;
import com.iplay.feastbooking.basic.BasicViewHolder;

/**
 * Created by admin on 2017/7/14.
 */

public class ProgressViewHolder extends BasicViewHolder {

    public final ProgressBar pb;

    public ProgressViewHolder(View itemView) {
        super(itemView);
        pb = (ProgressBar) itemView.findViewById(R.id.refresh_progress_bar);
    }

}
