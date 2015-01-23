package com.bainiaohe.dodo.main.fragments.info.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bainiaohe.dodo.main.fragments.info.model.DataItem;
import com.bainiaohe.dodo.main.fragments.info.view_holder.ViewHolder;

import java.util.List;

/**
 * Created by zhugongpu on 15/1/22.
 */
public class DataAdapter extends RecyclerView.Adapter<ViewHolder> {


    private List<DataItem> dataSet = null;
    private int dataItemLayout;

    /**
     * @param dataSet        所要显示的数据集
     * @param dataItemLayout 每个条目的布局id
     */

    public DataAdapter(List<DataItem> dataSet, int dataItemLayout) {
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
     * 设置数据
     *
     * @param data
     */
    public void setDataSet(List<DataItem> data) {
        this.dataSet.clear();
        this.dataSet = data;

        this.notifyDataSetChanged();
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        DataItem dataItem = dataSet.get(i);

        viewHolder.name.setText(dataItem.name);
        viewHolder.image.setImageDrawable(dataItem.icon);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 响应点击事件
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }
}
