package com.bainiaohe.dodo.info_detail;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.fragments.info.model.InfoItemModel;

/**
 * info详情页
 * 启动时，需要传入info item
 */
public class InfoDetailActivity extends Activity {
    /**
     * 启动activity时需要传输的参数名
     */
    public static final String PARAM_NAME = "INFO";
    private static final String TAG = "InfoDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail_activity);

        InfoItemModel infoItem = getIntent().getParcelableExtra(PARAM_NAME);

        Log.e(TAG, "Param:" + infoItem.name + " " + infoItem.isMarked);

        //TODO 显示UI Elements
    }

}
