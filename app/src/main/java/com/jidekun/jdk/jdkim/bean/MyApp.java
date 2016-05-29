package com.jidekun.jdk.jdkim.bean;


import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by JDK on 2016/5/22.
 */
public class MyApp {
    /**
     * MyApp.me = conn;
     * MyApp.username = username;
     * MyApp.account = username + "@qq.com";
     */
    //主机ip地址
    public static final String HOST = "192.168.11.30";
    //端口号
    public static final int PORT = 5222;
    //服务器名称6
    public static final String SERVER_NAME = "jdk-pc";
    public static String username;
    public static String account;
    public static XMPPConnection conn;
}
