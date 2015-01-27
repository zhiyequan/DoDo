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

                RongIMClient.UserInfo user1 = new RongIMClient.UserInfo("3", "韩梅梅", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");

                list.add(user1);

                RongIMClient.UserInfo user2 = new RongIMClient.UserInfo("2", "王雷雷", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");

                list.add(user2);

                RongIMClient.UserInfo user3 = new RongIMClient.UserInfo("4", "&纯果乐", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");
                list.add(user3);

                RongIMClient.UserInfo user4 = new RongIMClient.UserInfo("6", "lewiskit", "http://tp1.sinaimg.cn/3475942940/180/5693399301/1");
                list.add(user4);

                RongIMClient.UserInfo user5 = new RongIMClient.UserInfo("7", "hehe", "http://tp1.sinaimg.cn/3475942940/180/5693399301/1");
                list.add(user5);

                RongIMClient.UserInfo user6 = new RongIMClient.UserInfo("8", "shabi", "http://tp1.sinaimg.cn/3475942940/180/5693399301/1");
                list.add(user6);

                RongIMClient.UserInfo user7 = new RongIMClient.UserInfo("9", "↑haaha", "http://tp1.sinaimg.cn/3475942940/180/5693399301/1");
                list.add(user7);

                for (int i = 10; i <= 20; i++) {
                    RongIMClient.UserInfo user = new RongIMClient.UserInfo(i + "", i + "二货", "http://tp1.sinaimg.cn/3475942940/180/5693399301/1");
                    list.add(user);
                }

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
                RongIMClient.UserInfo me = new RongIMClient.UserInfo("1", "lewis", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");

                return me;
            }
        }, false);

    }


}
