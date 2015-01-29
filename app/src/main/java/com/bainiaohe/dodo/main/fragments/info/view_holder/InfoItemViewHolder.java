package com.bainiaohe.dodo.main.fragments.info.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bainiaohe.dodo.R;

/**
 * Created by zhugongpu on 15/1/22.
 */
public class InfoItemViewHolder extends RecyclerView.ViewHolder {

    public TextView name = null;//用户名
    public TextView time = null;//发表时间
    public ImageView avatarImage = null;//头像
    public ImageButton markButton = null;//赞
    public TextView content = null;//文本内容
    public ImageView imageView1 = null;//图片
    public ImageView imageView2 = null;
    public ImageView imageView3 = null;
    public Button shareButton = null;//转发
    public Button commentButton = null;//评论

    public InfoItemViewHolder(View itemView) {
        super(itemView);

        this.name = (TextView) itemView.findViewById(R.id.name);
        this.time = (TextView) itemView.findViewById(R.id.time);
        this.avatarImage = (ImageView) itemView.findViewById(R.id.avatar);
        this.markButton = (ImageButton) itemView.findViewById(R.id.mark);
        this.content = (TextView) itemView.findViewById(R.id.text_content);
        this.imageView1 = (ImageView) itemView.findViewById(R.id.image1);
        this.imageView2 = (ImageView) itemView.findViewById(R.id.image2);
        this.imageView3 = (ImageView) itemView.findViewById(R.id.image3);
        this.shareButton = (Button) itemView.findViewById(R.id.share);
        this.commentButton = (Button) itemView.findViewById(R.id.comment);
    }
}
