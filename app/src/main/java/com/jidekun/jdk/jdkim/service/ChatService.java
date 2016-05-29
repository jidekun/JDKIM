package com.jidekun.jdk.jdkim.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.jidekun.jdk.jdkim.activity.ChatActivity;
import com.jidekun.jdk.jdkim.bean.MyApp;
import com.jidekun.jdk.jdkim.db.MessageDao;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class ChatService extends Service {


    private ChatManager cm;

    @Override
    public void onCreate() {
        super.onCreate();
        cm = MyApp.conn.getChatManager();
        cm.addChatListener(cmLi);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        cm.removeChatListener(cmLi);
    }

    //监听chat对象
    private ChatManagerListener cmLi = new ChatManagerListener() {

        // 监听聊天 createdLocally true 本程序发起聊天 createChat false 别人过来找聊天
        @Override
        public void chatCreated(Chat chat, final boolean createdLocally) {
            if (!createdLocally) {
                chat.addMessageListener(mLis);
            }
            System.out.println("---监听chat聊天的创建--" + createdLocally);

        }
    };
    //监听message
    private MessageListener mLis = new MessageListener() {
        //接收消息
        @Override
        public void processMessage(Chat chat, final Message message) {
            //如果是消息类型
            if (Message.Type.chat.equals(message.getType())) {
                MessageDao md =new MessageDao(getBaseContext());
                md.saveReceMessage(message);
                System.out.println("服务中接收到消息" + message.getBody());
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
