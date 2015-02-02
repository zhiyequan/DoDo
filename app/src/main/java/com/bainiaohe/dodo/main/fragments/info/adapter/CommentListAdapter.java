package com.bainiaohe.dodo.main.fragments.info.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bainiaohe.dodo.main.fragments.info.model.CommentModel;
import com.bainiaohe.dodo.main.fragments.info.view_holder.CommentViewHolder;

import java.util.ArrayList;

/**
 * Created by zhugongpu on 15/2/2.
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private ArrayList<CommentModel> dataSet = null;
    private Context context = null;
    private int dataItemLayout;

    public CommentListAdapter(Context context, int dataItemLayout) {
        this.context = context;
        this.dataItemLayout = dataItemLayout;
        this.dataSet = new ArrayList<>();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(dataItemLayout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        //TODO

    }

    @Override
    public int getItemCount() {
        return this.dataSet.size();
    }


    public void addData(CommentModel comment) {
        this.dataSet.add(comment);
    }

    public void clearData() {
        this.dataSet.clear();
    }
}
