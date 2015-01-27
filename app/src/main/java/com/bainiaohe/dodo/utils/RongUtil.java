package com.bainiaohe.dodo.utils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Lewis on 2015/1/21.
 */
public class RongUtil {

    /**
     * 从服务器获取好友列表
     * 设置好友信息提供者
     * todo:存储到本地数据库
     */
    public static void setFriends() {
        RongIM.setGetFriendsProvider(new RongIM.GetFriendsProvider() {

            @Override
            public List<RongIMClient.UserInfo> getFriends() {
                List<RongIMClient.UserInfo> list = new ArrayList<RongIMClient.UserInfo>();
                RongIMClient.UserInfo user1 = new RongIMClient.UserInfo("20", "user1", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");


                list.add(user1);



                return list;
            }
        });

    }

    /**
     * 获取用户信息
     * 设置用户信息提供者
     * todo:将信息存储到本地数据库并判断，下次从本地数据库中直接读取
     */
    public static void setUserInfo() {

        RongIM.setGetUserInfoProvider(new RongIM.GetUserInfoProvider() {

            @Override
            public RongIMClient.UserInfo getUserInfo(String s) {
                RongIMClient.UserInfo me = new RongIMClient.UserInfo("30", "user2", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");

                return me;
            }
        }, false);


    }

}
