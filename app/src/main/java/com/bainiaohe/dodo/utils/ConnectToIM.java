package com.bainiaohe.dodo.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bainiaohe.dodo.main.MainActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by liuxiaoran on 2015/1/27.
 */
public class ConnectToIM {
    public final String TAG="ConnectToIm";
    private Context context;
    public ConnectToIM(Context context){
        this.context=context;
    }
    /**
     * IMKit SDK 调用第二步
     * <p/>
     * 建立与服务器之间的链接
     */
    public void connectToIM() {
        try {

            RongIM.connect("EximAJ6+SDwvlEzM4n1IomxkwFKUShKpTl4x4o92Obe6edRQJwHhNpq+PPJT7NYjzqG3K1xtepOqX2zmpXXdDw==", new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "----Login Success!");
                    RongUtil.setUserInfo();
                    RongUtil.setFriends();
                    context.startActivity(new Intent(context, MainActivity.class));
                }

                @Override
                public void onError(ErrorCode errorCode) {
                    Log.d(TAG, "---Login Failed----");
                    Toast.makeText(context, "服务器连接失败", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "The server has some error");
        }
    }

}
