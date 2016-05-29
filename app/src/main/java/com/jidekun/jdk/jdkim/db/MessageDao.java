package com.jidekun.jdk.jdkim.db;

import android.content.ContentValues;
import android.content.Context;

import com.jidekun.jdk.jdkim.bean.MyApp;
import com.jidekun.jdk.jdkim.bean.TypeData;
import com.jidekun.jdk.jdkim.utils.NickUtil;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by JDK on 2016/5/26.
 */
public class MessageDao {
    private Context context;

    public MessageDao(Context context) {
        this.context = context;
    }

    public void saveSendMessage(Message msg) {


        ContentValues values = new ContentValues();


        //发送者的ID 应该是代@的
        String account = msg.getFrom();
        //设置发送者的ID
        values.put(TypeData.FROM_ID, account);
        //发送者的昵称
        // String nick = NickUtil.getNick(context, account);
        // System.out.println("打印登陆是的Id"+ account+"打印username"+nick);
        String getTo = msg.getTo();
        String sessionName = NickUtil.getNick(context, getTo);

        //发送者的昵称
        values.put(TypeData.FROM_NICK, MyApp.username);
        //发送者的头像
        values.put(TypeData.FROM_AVATAR, 0);
        //消息体
        values.put(TypeData.BODY, msg.getBody());
        //发送消息的时间 为系统时间
        values.put(TypeData.TIME, System.currentTimeMillis());
        //发送消息的类型
        values.put(TypeData.TYPE, Message.Type.chat.toString());
        //状态
        values.put(TypeData.STATUS, "");
        //是否可读
        values.put(TypeData.UNREAD, 0);
        //会话ID 为接收者
        values.put(TypeData.SESSION_ID, msg.getTo());
        System.out.println("msg.getTO" + msg.getTo());
        values.put(TypeData.SESSION_NAME, sessionName);
        SmsDao smsDao = new SmsDao(context);
        smsDao.add(values);

    }

    public void saveReceMessage(Message msg) {
        //小头@itheima.com-->老王@itheima.com
        String from = NickUtil.filterAccount(msg.getFrom());
        ContentValues values = new ContentValues();
        values.put(TypeData.FROM_ID, from);
        String nick = NickUtil.getNick(context, from);
        values.put(TypeData.FROM_NICK, nick);
        values.put(TypeData.FROM_AVATAR, 0);
        values.put(TypeData.BODY, msg.getBody());
        values.put(TypeData.TIME, System.currentTimeMillis());
        values.put(TypeData.TYPE, Message.Type.chat.toString());
        values.put(TypeData.STATUS, "");
        values.put(TypeData.UNREAD, 0);
        //会话ID 为发送者
        values.put(TypeData.SESSION_ID, from);
        values.put(TypeData.SESSION_NAME, nick);
        SmsDao smsDao = new SmsDao(context);
        smsDao.add(values);
    }
}
