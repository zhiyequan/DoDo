package com.bainiaohe.dodo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bainiaohe.dodo.model.User;
import com.sea_monster.core.utils.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.UserInfo;

/**
 * Created by Lewis on 2015/1/30.
 */
public class DoDoContext {

    private static DoDoContext instance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private List<UserInfo> mUserInfos;

    public DoDoContext() {
    }


    /**
     * 获得一个DoDoContext 实例
     *
     * @return
     */
    public static DoDoContext getInstance() {

        if (instance == null) {
            instance = new DoDoContext();
        }

        return instance;
    }

    public void init(Context context) {
        mContext = context;
        //初始化 sharedPreferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }



    /**
     * 当获取数据之后
     *
     * @param userInfos
     */
    public void setFriends(ArrayList<UserInfo> userInfos) {

        this.mUserInfos = userInfos;

        /**
         * 使用融云的imkit接口，所以必须设置好友关系提供者
         * RongIM将调用此Provider 获取好友列表信息
         */
        RongIM.setGetFriendsProvider(new RongIM.GetFriendsProvider() {

            @Override
            public List<UserInfo> getFriends() {
                return mUserInfos;
            }
        });


        /**
         * 设置用户信息提供者.如果在聊天的过程中融云将调用此方法获取用户信息
         */
        RongIM.setGetUserInfoProvider(new RongIM.GetUserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {

                return getUserInfoById(s);
            }
        }, false);
    }


    /**
     * 通过userId 查找在好友列表中特定的人
     */
    public UserInfo getUserInfoById(String userId) {
        UserInfo userForReturn = null;

        if (!userId.equals("") && userId != null && mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {
                if (userInfo.getUserId().equals(userId)) {
                    userForReturn = userInfo;
                    break;
                }
            }
        }

        return userForReturn;

    }


    /**
     * 存储当前用户信息
     * 将user 存储到sharedpreference 之后如果需要的话直接通过getCurrentUser获得
     */
    public void saveCurrentUser(User user) {

        if (mSharedPreferences == null)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);

            String userBase64 = new String(Base64.encode(baos.toByteArray()));

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("currentUser", userBase64);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获得当前用户,之前的信息都存储在sharedpreferences里面在
     * 返回的是一个user实例
     */
    public User getCurrentUser() {

        User user = null;
        String userBase64 = mSharedPreferences.getString("currentUser", null);

        try {
            byte[] base64 = Base64.decode(userBase64);

            ByteArrayInputStream bais = new ByteArrayInputStream(base64);
            ObjectInputStream bis = new ObjectInputStream(bais);

            user = (User) bis.readObject();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }


}
