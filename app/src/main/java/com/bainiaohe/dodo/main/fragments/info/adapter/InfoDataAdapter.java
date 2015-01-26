package com.bainiaohe.dodo.main.fragments.info.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.info.model.InfoItem;
import com.bainiaohe.dodo.main.fragments.info.view_holder.InfoItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhugongpu on 15/1/22.
 */
public class InfoDataAdapter extends RecyclerView.Adapter<InfoItemViewHolder> {
    private static final String TAG = "DataAdapter";

    private List<InfoItem> dataSet = null;
    private int dataItemLayout;//每个item的布局id

    private Context context = null;

    /**
     * @param context
     * @param dataSet        所要显示的数据集
     * @param dataItemLayout 每个条目的布局id
     */

    public InfoDataAdapter(Context context, List<InfoItem> dataSet, int dataItemLayout) {
        this.context = context;
        this.dataSet = dataSet;
        this.dataItemLayout = dataItemLayout;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public InfoItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(dataItemLayout, viewGroup, false);
        return new InfoItemViewHolder(view);
    }

    /**
     * 设置数据(清除原有数据)，并通知更新UI
     *
     * @param data
     */
    public void setDataSet(List<InfoItem> data) {
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
    public void addDataItem(InfoItem dataItem) {
        this.dataSet.add(dataItem);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(InfoItemViewHolder infoItemViewHolder, int i) {

        InfoItem dataItem = dataSet.get(i);

        infoItemViewHolder.name.setText(dataItem.name);
        infoItemViewHolder.avatarImage.setImageDrawable(dataItem.avatarImage);
        infoItemViewHolder.content.setText(dataItem.text_content);

//      TODO 根绝dataItem.isMarked设置 infoItemViewHolder.markButton图标及响应事件

        //加载图片
        if (dataItem.imageUrls != null) {
            int imageCount = dataItem.imageUrls.size();
            //TODO resize and center crop
            //TODO 设置正在加载和加载失败的图标
            if (imageCount >= 1) {
                Picasso.with(context)
                        .load(dataItem.imageUrls.get(0))
                        .resizeDimen(R.dimen.medium_picture_width, R.dimen.medium_picture_height).centerInside()
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
                        .resize(200, 200).centerInside()
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
                        .resize(200, 200).centerInside()
                        .into(infoItemViewHolder.imageView3);
                infoItemViewHolder.imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO
                    }
                });
            }
        }

        //响应点击事件
        infoItemViewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 跳转到详情页
            }
        });

        infoItemViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 分享
            }
        });

        infoItemViewHolder.markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 根绝dataItem.isMarked决定
            }
        });


        infoItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 跳转到详情页,同content点击事件
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }
}