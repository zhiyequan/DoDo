package com.bainiaohe.dodo.main.fragments.info.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.info_detail.InfoDetailActivity;
import com.bainiaohe.dodo.main.fragments.info.model.InfoItemModel;
import com.bainiaohe.dodo.main.fragments.info.view.CommentListItem;
import com.bainiaohe.dodo.main.fragments.info.view_holder.InfoItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhugongpu on 15/1/22.
 */
public class InfoDataAdapter extends RecyclerView.Adapter<InfoItemViewHolder> {
    private static final String TAG = "DataAdapter";

    private List<InfoItemModel> dataSet = null;
    private int dataItemLayout;//每个item的布局id

    private Context context = null;

    /**
     * @param context
     * @param dataSet        所要显示的数据集
     * @param dataItemLayout 每个条目的布局id
     */

    public InfoDataAdapter(Context context, List<InfoItemModel> dataSet, int dataItemLayout) {
        this.context = context;
        this.dataSet = dataSet;
        this.dataItemLayout = dataItemLayout;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public InfoItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(dataItemLayout, viewGroup, false);
        return new InfoItemViewHolder(view);
    }

    /**
     * 设置数据(清除原有数据)，并通知更新UI
     *
     * @param data
     */
    public void setDataSet(List<InfoItemModel> data) {
        this.clearData();
        this.dataSet = data;

        this.notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clearData() {
        if (this.dataSet != null)
            this.dataSet.clear();
    }

    /**
     * 添加数据项
     *
     * @param dataItem
     */
    public void addDataItem(InfoItemModel dataItem) {
        this.dataSet.add(dataItem);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(final InfoItemViewHolder infoItemViewHolder, int position) {

        final InfoItemModel dataItem = dataSet.get(position);

        infoItemViewHolder.name.setText(dataItem.name);
        infoItemViewHolder.time.setText(dataItem.time);
        infoItemViewHolder.content.setText(dataItem.text_content);
        //加载头像
        Picasso.with(context).load(dataItem.avatarImage).placeholder(R.drawable.default_avatar).into(infoItemViewHolder.avatarImage);

        //根据dataItem.isMarked设置 infoItemViewHolder.markButton图标及响应事件
        if (dataItem.isMarked) {
            Picasso.with(context).load(R.drawable.marked).into(infoItemViewHolder.markButton);
        } else {
            Picasso.with(context).load(R.drawable.unmarked).into(infoItemViewHolder.markButton);
            infoItemViewHolder.markButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //TODO 向服务器发送请求


                    Picasso.with(context).load(R.drawable.marked).into(infoItemViewHolder.markButton);
                    dataItem.isMarked = true;//设置为已经mark
                }
            });
        }


        //加载图片
        if (dataItem.imageUrls != null) {
            int imageCount = dataItem.imageUrls.size();
            if (imageCount >= 1) {
                Picasso.with(context)
                        .load(dataItem.imageUrls.get(0))
                        .placeholder(R.drawable.picture_holder)
                        .error(R.drawable.picture_load_failed)
                        .resizeDimen(R.dimen.medium_picture_size, R.dimen.medium_picture_size).centerInside()
                        .into(infoItemViewHolder.imageView1);

                infoItemViewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO 响应点击事件
                    }
                });
            }
            if (imageCount >= 2) {
                Picasso.with(context)
                        .load(dataItem.imageUrls.get(1))
                        .placeholder(R.drawable.picture_holder)
                        .error(R.drawable.picture_load_failed)
                        .resizeDimen(R.dimen.medium_picture_size, R.dimen.medium_picture_size).centerInside()
                        .into(infoItemViewHolder.imageView2);
                infoItemViewHolder.imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo
                    }
                });
            }
            if (imageCount >= 3) {//当多于三张图片时，只显示前三张
                Picasso.with(context)
                        .load(dataItem.imageUrls.get(2))
                        .placeholder(R.drawable.picture_holder)
                        .error(R.drawable.picture_load_failed)
                        .resizeDimen(R.dimen.medium_picture_size, R.dimen.medium_picture_size).centerInside()
                        .into(infoItemViewHolder.imageView3);
                infoItemViewHolder.imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO
                    }
                });
            }
        }

//        CommentListAdapter commentListAdapter = new CommentListAdapter(context, R.layout.item_layout_comment);
//        infoItemViewHolder.commentList.setAdapter(commentListAdapter);
        //加载评论列表
        if (dataItem.comments != null) {

            for (int i = 0; i < dataItem.comments.size(); i++) {
                infoItemViewHolder.commentList
                        .addView(CommentListItem.genView(context, dataItem.comments.get(i)));
            }

            infoItemViewHolder.commentList.invalidate();
        }

        //响应点击事件
        infoItemViewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到详情页
                jumpToDetailPage(dataItem);
            }
        });

        infoItemViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 分享
            }
        });

        infoItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到详情页,同content点击事件
                jumpToDetailPage(dataItem);
            }
        });
    }

    /**
     * 点击info item之后跳转到info详情页
     *
     * @param infoItem
     */
    private void jumpToDetailPage(InfoItemModel infoItem) {
        Intent intent = new Intent(context, InfoDetailActivity.class);
        //传参
        intent.putExtra(InfoDetailActivity.PARAM_NAME, infoItem);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }
}
