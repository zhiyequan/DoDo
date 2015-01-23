package com.bainiaohe.dodo.main.fragments.info.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bainiaohe.dodo.R;

/**
 * Created by zhugongpu on 15/1/22.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView name = null;
    public ImageView image = null;

    public ViewHolder(View itemView) {
        super(itemView);

        this.name = (TextView) itemView.findViewById(R.id.title);
        this.image = (ImageView) itemView.findViewById(R.id.icon);
    }
}
