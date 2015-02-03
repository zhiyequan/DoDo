package com.bainiaohe.dodo.main.fragments.info.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.info.model.CommentModel;
import com.squareup.picasso.Picasso;

/**
 * Created by zhugongpu on 15/2/2.
 */
public class CommentListItem {

    public static View genView(Context context, CommentModel comment) {
        return genView(context, comment.avatar, comment.name, comment.time, comment.content);
    }

    public static View genView(Context context, String avatar, String name, String time, String content) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_comment, null);
        //加载头像
        Picasso.with(context).load(avatar)
                .resizeDimen(R.dimen.medium_picture_size, R.dimen.medium_picture_size).centerInside()
                .into((android.widget.ImageView) view.findViewById(R.id.avatar));

        ((TextView) view.findViewById(R.id.name)).setText(name);
        ((TextView) view.findViewById(R.id.time)).setText(time);
        ((TextView) view.findViewById(R.id.content)).setText(content);
        return view;
    }

}
