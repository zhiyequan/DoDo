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

            RongIM.connect("055aftfm1FNEcOH7G4eCLm9m2GF2Kx+PJbNPy2eR2UQfTS3xdHPBD1gCsxd/DpH3hYtyVLroK9aghRkAkaOJ+w==", new RongIMClient.ConnectCallback()  {
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "----Login Success!");
                    RongUtil ru = new RongUtil();
                    ru.setFriends();
                    ru.setUserInfo();
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
