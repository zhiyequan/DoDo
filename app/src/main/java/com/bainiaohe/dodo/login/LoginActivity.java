package com.bainiaohe.dodo.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.MainActivity;
import com.bainiaohe.dodo.register.RegisterActivity;
import com.bainiaohe.dodo.utils.RongUtil;
import com.bainiaohe.dodo.utils.UserService;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by xiaoran on 2015/1/19.
 */
public class LoginActivity extends Activity implements View.OnClickListener, PlatformActionListener, Handler.Callback {

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private static String TAG = "LoginActivity";
    SharedPreferences sharedPreferences;
    private Button login_btn;
    private EditText login_phone;
    private EditText login_pw;
    private Button qq_login_button;
    private Button weixin_login_button;
    private Button weibo_login_button;
    private String nickName;
    private String otherplatformId;
    private int otherplatformType=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //store user info sharepreferences
        sharedPreferences = LoginActivity.this.getSharedPreferences("user", MODE_PRIVATE);
        //init sharesdk
        ShareSDK.initSDK(this);
        login_btn = (Button) findViewById(R.id.login_login_btn);
        login_btn.setOnClickListener(this);
        login_phone = (EditText) findViewById(R.id.login_phone);
        login_pw = (EditText) findViewById(R.id.login_pw);
        qq_login_button = (Button) findViewById(R.id.qq_login_btn);
        weixin_login_button = (Button) findViewById(R.id.weixin_login_btn);
        weibo_login_button = (Button) findViewById(R.id.weibo_login_btn);
        qq_login_button.setOnClickListener(this);
        weixin_login_button.setOnClickListener(this);
        weibo_login_button.setOnClickListener(this);
        if (sharedPreferences.getBoolean("ischecked", false)) {
            //TODO:进入系统
            Intent intent = new Intent();
            String phone = sharedPreferences.getString("phone", "");
            startActivity(intent);
        }


    }

    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_login_btn) {
            //click login button
            String phone=login_phone.getText().toString();
            String pw=login_pw.getText().toString();
            if (phone != null && pw != null) {
                new LoginTask().execute(login_phone.getText().toString(), login_pw.getText().toString());

            }
            ConnectToIM();

        } else {
            //click  other platform button
            if (view.getId() == R.id.qq_login_btn) {
                otherplatformType = 1;
                // 获取平台列表
                Platform[] tmp = ShareSDK.getPlatformList();
                Platform platform = null;
                for (Platform p : tmp) {
                    String name = p.getName();
                    if (name.equals("QQ")) {
                        platform = p;
                        break;
                    }
                }
                platform.setPlatformActionListener(this);
                platform.showUser(null);


            } else if (view.getId() == R.id.weixin_login_btn) {
                otherplatformType = 2;

            } else {
                //weibo login
                otherplatformType = 3;
                // 获取平台列表
                Platform[] tmp = ShareSDK.getPlatformList();
                Platform platform = null;
                for (Platform p : tmp) {
                    String name = p.getName();
                    if (name.equals("SinaWeibo")) {
                        platform = p;
                        break;
                    }
                }
                platform.setPlatformActionListener(this);
                platform.showUser(null);

            }

        }
    }

    /**
     * IMKit SDK 调用第二步
     * <p/>
     * 建立与服务器之间的链接
     */

    private void ConnectToIM() {
        try {

            RongIM.connect("EximAJ6+SDwvlEzM4n1IomxkwFKUShKpTl4x4o92Obe6edRQJwHhNpq+PPJT7NYjzqG3K1xtepOqX2zmpXXdDw==", new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "----Login Success!");
                    RongUtil.setUserInfo();
                    RongUtil.setFriends();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                }

                @Override
                public void onError(ErrorCode errorCode) {
                    Log.d(TAG, "---Login Failed----");
                    Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "The server has some error");
        }


    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_LOGIN: {

                String text = getString(R.string.logining, msg.obj);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();


            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
//            login(platform.getName(), platform.getDb().getUserId(), res);

            //res  是用户资料  ，返回的json数据
            Log.v(TAG, res.toString());
            nickName = platform.getDb().getUserName();
            otherplatformId = platform.getDb().getUserId();
            // 判断这个平台的用户是否注册过
            boolean ret = UserService.isRegisted(otherplatformId);
            if (ret) {
                //用户注册过
                //TODO:进入系统
                Intent intent = new Intent();
                //用户ID
                String userId = UserService.userId;
                startActivity(intent);

            } else {
                //用户没注册过，进入注册流程
                Toast.makeText(this, "您未在度度注册，请注册", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("otherplatformId", otherplatformId);
                intent.putExtra("otherplatformType",otherplatformType);
                startActivity(intent);

            }
        }

    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    class LoginTask extends AsyncTask {
        ProgressDialog dialog;
        String phone;
        String pw;

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("请等待");
            dialog.setMessage("Logging");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        /**
         * object数组中为phone 、 pw
         */
        protected Object doInBackground(Object[] objects) {
            phone = objects[0].toString();
            pw = objects[1].toString();
            String result = UserService.userLogin(phone, pw);
            return result;
        }

        @Override
        protected void onPostExecute(Object result) {
            dialog.cancel();
            Toast.makeText(LoginActivity.this, result.toString(), Toast.LENGTH_LONG).show();
            SharedPreferences.Editor edit = sharedPreferences.edit();
            if (result.equals("success")) {

                edit.putBoolean("ischecked", true);
                edit.putString("phone", phone);
                edit.putString("pw", pw);
            } else {
                edit.putBoolean("ischecked", false);
            }

        }
    }

}
