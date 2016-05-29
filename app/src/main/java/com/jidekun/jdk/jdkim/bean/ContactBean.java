package com.jidekun.jdk.jdkim.bean;

import java.io.Serializable;

/**
 * Created by JDK on 2016/5/25.
 */
//实现Serializable接口 ,可以当对象添加到intent传递
public class ContactBean implements Serializable {

    public String account;
    public String nick;
    public int avatar;
    public String sort;

    @Override
    public String toString() {
        return "ContactBean{" +
                "account='" + account + '\'' +
                ", nick='" + nick + '\'' +
                ", avatar=" + avatar +
                ", sort='" + sort + '\'' +
                '}';
    }
}
