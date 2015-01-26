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
import com.bainiaohe.dodo.main.fragments.info.adapter.InfoDataAdapter;
import com.bainiaohe.dodo.main.fragments.info.animator.CustomItemAnimator;
import com.bainiaohe.dodo.main.fragments.info.data_loader.LoadDataAsyncTask;
import com.bainiaohe.dodo.main.fragments.info.model.InfoItem;

import java.util.ArrayList;

public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";

    /**
     * 加载数据
     */
    private LoadDataAsyncTask.DataLoader dataLoader = new LoadDataAsyncTask.DataLoader() {
        private ArrayList<InfoItem> dataSet = new ArrayList<InfoItem>();

        @Override
        public void doInBackground() {
//TODO dummy implementation
            dataSet.clear();//清除数据

            //TODO 请求数据
            for (int i = 0; i < 10; i++) {
                InfoItem item = new InfoItem();

                item.name = "name";
                item.avatarImage = getResources().getDrawable(R.drawable.ic_launcher);
                item.imageUrls = new ArrayList<String>();
                item.imageUrls.add("http://square.github.io/picasso/static/sample.png");
                dataSet.add(item);
            }
        }

        @Override
        public void onPostExecute() {

            Log.e(TAG, "onPostExecute : " + dataSet.size());

            adapter.setDataSet((ArrayList<InfoItem>) dataSet.clone());//需要clone，否则接收到的数据为空
            //TODO recycler view 加载动画

            //停止刷新
            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    };

    protected View view;//fragment view
    private RecyclerView recyclerView = null;//相当于list view
    private SwipeRefreshLayout swipeRefreshLayout = null;//下拉刷新
    private InfoDataAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_info, container, false);

        adapter = new InfoDataAdapter(getActivity(), new ArrayList<InfoItem>(), R.layout.item_layout_info);

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
