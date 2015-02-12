package com.bainiaohe.dodo.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bainiaohe.dodo.DoDoContext;
import com.bainiaohe.dodo.add_friends.PersonProfile;
import com.bainiaohe.dodo.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuxiaoran on 2015/2/3.
 */
public class AddFriendsService {


    //想要添加的好友
    public static User user = new User();
    public static String name;
    public static String nickName;
    public static String sex;
    public static String phone;
    public static String email;
    public static String avatar;
    public static String userId;
    public static AsyncHttpClient client = new AsyncHttpClient();
    public static Context pContext;
    public final static String TAG = "AddFriendsService";

    /**
     * 得到想要添加的好友的信息
     *
     * @param
     */
    public static void getUserInfo(String userPhone, Context context) {
        pContext = context;
        Log.v(TAG, userPhone);
        String url = URLConstants.USER_INFO + "?phone=" + userPhone;
        Log.v(TAG, url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    if (statusCode == 0) {
                        JSONObject object = new JSONObject(new String(responseBody));
                        userId = object.getString("id");
                        name = object.getString("name");
                        nickName = object.getString("nickname");
                        sex = object.getString("sex");
                        phone = object.getString("phone");
                        email = object.getString("email");
                        avatar = object.getString("avatar");
                        user.setUserId(userId);
                        user.setName(name);
                        user.setNickName(nickName);
                        user.setSex(sex);
                        user.setPhone(phone);
                        user.setEmail(email);
                        user.setAvatar(avatar);
                        Log.v(TAG, "success");
                        Intent intent = new Intent(pContext, PersonProfile.class);
                        pContext.startActivity(intent);
                    } else {


                    }

                } catch (JSONException e) {
                    Log.v(TAG, e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "failed");
            }
        });

    }

    public static void addFriend(String userId) {
        RequestParams params = new RequestParams();
        params.put("id", DoDoContext.getInstance().getCurrentUser().getUserId());
        params.put("friend_id", userId);
        client.post(URLConstants.ADD_FRIEND, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
