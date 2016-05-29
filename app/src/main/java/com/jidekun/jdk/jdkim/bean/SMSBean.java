package com.jidekun.jdk.jdkim.bean;

/**
 * Created by JDK on 2016/5/26.
 */
public class SMSBean {
    public String FROM_ID;
    public String FROM_NICK;
    public String FROM_AVATAR;
    public String BODY;
    public String TYPE;// chat
    public long TIME;
    public String STATUS;
    public int UNREAD;
    public String SESSION_ID;
    public String SESSION_NAME;

    @Override
    public String toString() {
        return "SMSBean{" +
                "BODY='" + BODY + '\'' +
                ", FROM_ID='" + FROM_ID + '\'' +
                ", FROM_NICK='" + FROM_NICK + '\'' +
                ", FROM_AVATAR='" + FROM_AVATAR + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", TIME='" + TIME + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", UNREAD='" + UNREAD + '\'' +
                ", SESSION_ID='" + SESSION_ID + '\'' +
                ", SESSION_NAME='" + SESSION_NAME + '\'' +
                '}';
    }
}
