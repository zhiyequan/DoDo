package com.bainiaohe.dodo.main.fragments.info.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bainiaohe.dodo.main.fragments.info.model.InfoItem;
import com.bainiaohe.dodo.main.fragments.info.view_holder.ViewHolder;

import java.util.List;

/**
 * Created by zhugongpu on 15/1/22.
 */
public class DataAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "DataAdapter";

    private List<InfoItem> dataSet = null;
    private int dataItemLayout;//每个item的布局id

    /**
     * @param dataSet        所要显示的数据集
     * @param dataItemLayout 每个条目的布局id
     */

    public DataAdapter(List<InfoItem> dataSet, int dataItemLayout) {
        this.dataSet = dataSet;
        this.dataItemLayout = dataItemLayout;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(dataItemLayout, viewGroup, false);
        return new ViewHolder(view);
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        InfoItem dataItem = dataSet.get(i);

        viewHolder.name.setText(dataItem.name);
        viewHolder.avatarImage.setImageDrawable(dataItem.avatarImage);
        //TODO set ui elements in view holder

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 响应点击事件
            }
        });
        //TODO 处理图片点击事件

    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }
}
