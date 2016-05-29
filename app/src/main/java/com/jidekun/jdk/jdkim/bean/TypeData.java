package com.jidekun.jdk.jdkim.bean;

import android.net.Uri;

/**
 * Created by JDK on 2016/5/21.
 */
public class TypeData {
    public static final String MSG_TYPE_REGISTER = "register";// 注册
    public static final String MSG_TYPE_LOGIN = "login";// 登录
    public static final String MSG_TYPE_LOGIN_OUT = "loginout";// 登出
    public static final String MSG_TYPE_CHAT_P2P = "chatp2p";// 聊天
    public static final String MSG_TYPE_CHAT_ROOM = "chatroom";// 群聊
    public static final String MSG_TYPE_OFFLINE = "offline";// 下线
    public static final String MSG_TYPE_SUCCESS = "success";//成功
    public static final String MSG_TYPE_BUDDY_LIST = "buddylist";// 好友
    public static final String MSG_TYPE_FAILURE = "failure";// 失败


    public static final String FROM_ID = "from_id";// 发送
    public static final String FROM_NICK = "from_nick";
    public static final String FROM_AVATAR = "from_avatar";
    public static final String BODY = "body";
    public static final String TYPE = "type";// chat
    public static final String TIME = "time";
    public static final String STATUS = "status";
    public static final String UNREAD = "unread";

    public static final String SESSION_ID = "session_id";
    public static final String SESSION_NAME = "session_name";


    public static final String ACCOUNT = "account";
    public static final String NICK = "nick";
    public static final String AVATAR = "avatar";
    public static final String SORT = "sort";// 拼音


    //将访问内容提供者的名称,避免硬编码
    private static final String AUTHORITY = "com.jidekun.jdk.jdkim";
    public static final String TABLE = "contacts";
    public static final String TABLE1="SMS";
    //访问该contentprovider的uri地址
    public static final Uri CONTACT_URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE);
    public static final Uri SMS_URI=Uri.parse("content://" + AUTHORITY + "/"+TABLE1);
}
