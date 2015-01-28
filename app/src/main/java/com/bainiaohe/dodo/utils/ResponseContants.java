package com.bainiaohe.dodo.utils;

/**
 * 服务器返回的Response中各字段名称
 * Created by zhugongpu on 15/1/28.
 */
public class ResponseContants {


    /**
     * 服务器返回结果中的 status 字段
     * 通用
     */
    public static final String RESPONSE_STATUS = "status";

    /**
     * 服务器返回结果中的 message 字段
     * 通用
     */
    public static final String RESPONSE_MESSAGE = "message";


    /**
     * 服务器返回结果中的 消息列表 字段
     * 仅限 /ground/fetch 使用
     */
    public static final String RESPONSE_MESSAGES = "messages";

    /**
     * 服务器返回结果中的 用户id 字段
     */
    public static final String RESPONSE_ID = "id";

    /**
     * 服务器返回结果中的 用户token 字段
     */
    public static final String RESPONSE_TOKEN = "token";

    /**
     * 服务器返回结果中的 name 字段
     */
    public static final String RESPONSE_NAME = "name";

    /**
     * 服务器返回结果中的 nickname 字段
     */
    public static final String RESPONSE_NICKNAME = "nickname";


    /**
     * 服务器返回结果中的 用户sex 字段
     */
    public static final String RESPONSE_SEX = "sex";


    /**
     * 服务器返回结果中的 用户phone 字段
     */
    public static final String RESPONSE_PHONE = "phone";

    /**
     * 服务器返回结果中的 用户email 字段
     */
    public static final String RESPONSE_EMAIL = "email";

    /**
     * 服务器返回结果中的 用户avatar 字段
     */
    public static final String RESPONSE_AVATAR = "avatar";


    /**
     * 服务器返回结果中的 是否为新用户 字段
     */
    public static final String RESPONSE_NEW = "new";


    /**
     * 服务器返回结果中的 好友列表 字段
     */
    public static final String RESPONSE_FRIENDS = "friends";


}
