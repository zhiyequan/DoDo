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
import com.bainiaohe.dodo.utils.URLConstants;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";

    /**
     * 加载数据
     */
    private LoadDataAsyncTask.DataLoader dataLoader = new LoadDataAsyncTask.DataLoader() {
        private ArrayList<InfoItem> dataSet = new ArrayList<InfoItem>();

        /**
         * 从服务器获取Info列表
         */
        private void fetchInfo() {
            String url = URLConstants.FETCH_INFO_LIST + "?id=" + 2;//TODO 仅用于测试
//                    + UserService.userId;

            //请求数据
            AsyncHttpGet request = new AsyncHttpGet(url);
            AsyncHttpClient.getDefaultInstance().executeJSONObject(request, new AsyncHttpClient.JSONObjectCallback() {
                @Override
                public void onCompleted(Exception e, AsyncHttpResponse source, JSONObject result) {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    } else {
                        Log.e(TAG, "fetch info : " + result);

                        try {
                            JSONArray messages = result.getJSONArray("messages");
                            for (int i = 0; i < messages.length(); i++) {
                                JSONObject message = messages.getJSONObject(i);

                                //TODO 添加数据到data set
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            });
        }

        @Override
        public void doInBackground() {

            dataSet.clear();//清除数据

            //请求数据
            fetchInfo();

//TODO dummy implementation
            /*
            for (int i = 0; i < 10; i++) {
                InfoItem item = new InfoItem();

                item.name = "name";
                item.avatarImage = getResources().getDrawable(R.drawable.ic_launcher);
                item.text_content = "阿飞你说的工商局的开个会按时发货就开始数据库的规划上课按时发货就卡死凤凰就开始的粉红色空间的水电费后就开始地方后就开始的供货商的健康OS读后感会计师的规划 阿斯顿黑金卡是符合加咖啡和科技阿飞hjfsdfhskdjgasdhjkafhjka 啊实打实的";
                item.imageUrls = new ArrayList<>();
                item.imageUrls.add("http://square.github.io/picasso/static/sample.png");
                item.imageUrls.add("http://www.baidu.com/img/bdlogo.png");
                item.imageUrls.add("http://square.github.io/picasso/static/sample.png");
                item.imageUrls.add("http://square.github.io/picasso/static/debug.png");

                dataSet.add(item);
            }*/
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
