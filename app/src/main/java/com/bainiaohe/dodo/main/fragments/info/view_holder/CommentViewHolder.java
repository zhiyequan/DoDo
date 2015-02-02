package com.bainiaohe.dodo.main.fragments.info.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bainiaohe.dodo.R;

/**
 * Created by zhugongpu on 15/2/2.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {
    public ImageView avatar = null;
    public TextView name = null;
    public TextView time = null;
    public TextView comment = null;

    public CommentViewHolder(View view) {
        super(view);

        this.avatar = (ImageView) view.findViewById(R.id.avatar);
        this.name = (TextView) view.findViewById(R.id.name);
        this.time = (TextView) view.findViewById(R.id.time);
        this.comment = (TextView) view.findViewById(R.id.comment);
    }
}
