package com.bainiaohe.dodo;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.UserInfo;

/**
 * Created by Lewis on 2015/1/30.
 */
public class DoDoContext {

    private static DoDoContext instance;

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

}
