package com.bainiaohe.dodo;

import android.app.Application;
import io.rong.imkit.RongIM;

/**
 * Created by Lewis on 2015/1/20.
 */
public class DoDoApplication extends Application {

    //APP_KEY from Rong IM
    public static String RONG_APP_KEY = "bmdehs6pdv7ds";

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 融云初始化
         * 第三个参数是自定义的push图标
         */
        RongIM.init(this);

    }
}
