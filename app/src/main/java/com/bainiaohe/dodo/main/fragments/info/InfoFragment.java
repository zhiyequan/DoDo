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
import com.bainiaohe.dodo.main.fragments.info.model.InfoItem;
import com.bainiaohe.dodo.utils.ResponseContants;
import com.bainiaohe.dodo.utils.URLConstants;
import com.bainiaohe.dodo.utils.UserService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";

    protected View view;//fragment view
    private RecyclerView recyclerView = null;//相当于list view
    private SwipeRefreshLayout swipeRefreshLayout = null;//下拉刷新
    private FloatingActionButton floatingActionButton = null;//
    private InfoDataAdapter adapter = null;
    private boolean isLoadingData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_info, container, false);

        adapter = new InfoDataAdapter(getActivity(), new ArrayList<InfoItem>(), R.layout.item_layout_info);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new CustomItemAnimator());
        recyclerView.setAdapter(adapter);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_action_button_to_top);
        floatingActionButton.attachToRecyclerView(recyclerView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to top
                recyclerView.smoothScrollToPosition(0);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    floatingActionButton.hide();
                } else
                    floatingActionButton.show();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_accent));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh");
                loadData();
            }
        });

        loadData();//加载数据
        return view;
    }

    /**
     * 加载测试数据
     * 仅用于测试
     */
    private void dummyLoading() {

        for (int i = 0; i < 1000; i++) {
            InfoItem item = new InfoItem();

            item.name = "" + i;
            item.time = "just now";
            item.avatarImage = "http://b.hiphotos.baidu.com/image/pic/item/d53f8794a4c27d1e1be708c318d5ad6edcc438f7.jpg";
            item.text_content = "阿飞你说的工商局的开个会按时发货就开始数据库的规划上课按时发货就卡死凤凰就开始的粉红色空间的水电费后就开始地方后就开始的供货商的健康OS读后感会计师的规划 阿斯顿黑金卡是符合加咖啡和科技阿飞hjfsdfhskdjgasdhjkafhjka 啊实打实的";
            item.imageUrls = new ArrayList<>();
            item.imageUrls.add("http://square.github.io/picasso/static/sample.png");
            item.imageUrls.add("http://www.baidu.com/img/bdlogo.png");
            item.imageUrls.add("http://square.github.io/picasso/static/sample.png");
            item.imageUrls.add("http://square.github.io/picasso/static/debug.png");

            adapter.addDataItem(item);
        }
    }

    /**
     * 异步加载数据
     * 可防止重复加载
     */
    private void loadData() {
        if (isLoadingData) return;
//        Log.e(TAG, Thread.currentThread().getName());
        //设置正在加载数据，防止重复加载
        isLoadingData = true;

        swipeRefreshLayout.setRefreshing(true);

        //加载数据
        String url = URLConstants.FETCH_INFO_LIST + "?id="
//                + 2;//TODO 仅用于测试
                + UserService.userId;

        Log.e(TAG, "URL: " + url);

        //请求数据
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new DataAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(TAG, "onSuccess " + statusCode);

                dummyLoading();//TODO test code

                try {
                    //从byte[]转为JSONObject
                    JSONObject result = new JSONObject(new String(responseBody));

                    Log.e(TAG, "result : " + result);

                    final JSONArray messages = result.getJSONArray(ResponseContants.RESPONSE_MESSAGES);
                    for (int i = 0; i < messages.length(); i++) {
                        final JSONObject message = messages.getJSONObject(i);

                        //添加数据到adapter
                        if (message.has(ResponseContants.RESPONSE_MESSAGES_ID))//TODO 检验数据完整性
                            adapter.addDataItem(new InfoItem() {
                                {
                                    this.name = message.getString(ResponseContants.RESPONSE_MESSAGES_NAME);
                                    this.avatarImage = message.getString(ResponseContants.RESPONSE_MESSAGES_AVATAR);
                                    this.time = message.getString(ResponseContants.RESPONSE_MESSAGES_TIME);
                                    this.text_content = message.getString(ResponseContants.RESPONSE_MESSAGES_CONENT);

                                    JSONArray imageURLs = message.getJSONArray(ResponseContants.RESPONSE_MESSAGES_IMAGES);
                                    int imagesCount = imageURLs.length();
                                    if (imagesCount > 0)
                                        this.imageUrls = new ArrayList<>();
                                    for (int i = 0; i < imagesCount; i++)
                                        this.imageUrls.add(imageURLs.getString(i));

                                    this.isMarked = message.getBoolean(ResponseContants.RESPONSE_MESSAGES_IS_MARKED);
                                }
                            });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.e(TAG, "onFailure");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e(TAG, "onFinish");

                isLoadingData = false;
                adapter.notifyDataSetChanged();
                //停止动画
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
