package com.bainiaohe.dodo.utils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis on 2015/1/21.
 */
public class RongUtil {


    private List<RongIMClient.UserInfo> mUserInfos;

    public RongUtil() {

        mUserInfos = new ArrayList();
        mUserInfos.add(new RongIMClient.UserInfo("20", "user1", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png"));


    }




    /**
     * 从服务器获取好友列表
     * 设置好友信息提供者
     * todo:存储到本地数据库
     */
    public void setFriends() {
        RongIM.setGetFriendsProvider(new RongIM.GetFriendsProvider() {

            @Override
            public List<RongIMClient.UserInfo> getFriends() {


                return mUserInfos;
            }
        });

    }

    /**
     * 获取用户信息
     * 设置用户信息提供者
     * todo:将信息存储到本地数据库并判断，下次从本地数据库中直接读取
     */
    public void setUserInfo() {

        RongIM.setGetUserInfoProvider(new RongIM.GetUserInfoProvider() {

            @Override
            public RongIMClient.UserInfo getUserInfo(String s) {

                return getUserById(s);
            }
        }, false);


    }


    private RongIMClient.UserInfo getUserById(String userId) {
        RongIMClient.UserInfo userforReturn = null;

        if (userId != null && !userId.equals("") && mUserInfos != null) {

            for (RongIMClient.UserInfo userinfo : mUserInfos) {
                if (userId.equals(userinfo.getUserId())) {
                    userforReturn = userinfo;
                    break;

                }

            }


        }

        return userforReturn;

    }


}
