package com.bainiaohe.dodo.main.fragments.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.info.adapter.DataAdapter;
import com.bainiaohe.dodo.main.fragments.info.animator.CustomItemAnimator;
import com.bainiaohe.dodo.main.fragments.info.data_loader.LoadDataAsyncTask;
import com.bainiaohe.dodo.main.fragments.info.model.DataItem;

import java.util.ArrayList;

public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";
    private LoadDataAsyncTask.DataLoader dataLoader = new LoadDataAsyncTask.DataLoader() {
        private ArrayList<DataItem> dataSet = new ArrayList<DataItem>();

        @Override
        public void doInBackground() {
//TODO dummy implementation
            dataSet.clear();//清除数据

            for (int i = 0; i < 10; i++) {
                DataItem item = new DataItem();

                item.name = "name";
                item.avatarImage = getResources().getDrawable(R.drawable.ic_launcher);
                dataSet.add(item);
            }
        }

        @Override
        public void onPostExecute() {

            Log.e(TAG, "onPostExecute : " + dataSet.size());

            adapter.setDataSet((ArrayList<DataItem>) dataSet.clone());//需要clone，否则接收到的数据为空
            //TODO 加载动画
            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    };
    /**
     * view of Fragment
     */
    protected View view;
    private RecyclerView recyclerView = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private DataAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_info, container, false);

        adapter = new DataAdapter(new ArrayList<DataItem>(), R.layout.item_layout_info);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new CustomItemAnimator());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_accent));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadDataAsyncTask(dataLoader).execute();
            }
        });

        new LoadDataAsyncTask(dataLoader).execute();//加载数据
        return view;
    }


}
