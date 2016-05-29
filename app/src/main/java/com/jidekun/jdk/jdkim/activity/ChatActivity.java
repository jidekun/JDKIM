package com.jidekun.jdk.jdkim.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jidekun.jdk.jdkim.R;
import com.jidekun.jdk.jdkim.bean.ContactBean;
import com.jidekun.jdk.jdkim.bean.MyApp;
import com.jidekun.jdk.jdkim.bean.SMSBean;
import com.jidekun.jdk.jdkim.bean.TypeData;
import com.jidekun.jdk.jdkim.db.ContactDao;
import com.jidekun.jdk.jdkim.db.MessageDao;
import com.jidekun.jdk.jdkim.db.SmsDao;
import com.jidekun.jdk.jdkim.service.ChatService;
import com.jidekun.jdk.jdkim.utils.StringUtils;
import com.jidekun.jdk.jdkim.utils.TimeFormater;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChatActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.lv_chat)
    ListView lvChat;
    @InjectView(R.id.et_chat)
    EditText etChat;
    @InjectView(R.id.et_submit)
    Button etSubmit;
    @InjectView(R.id.tv_chat_title)
    TextView tvChatTitle;
    //private ContactBean contactBean;
    private Chat chat;
    private String toAccount;
    private ChatManager cm1;
    private String session_id;
    private ArrayList<SMSBean> session;
    private ChatAdapter chatAda;
    //监听聊天的创建
    private MyObserver mMyObserver = new MyObserver(new Handler());
    private String account;

    private class MyObserver extends ContentObserver {

        public MyObserver(Handler handler) {
            super(handler);
        }


        @Override
        public void onChange(boolean selfChange, Uri uri) {
            //当数据库增删改时 刷新数据
            setAdapterOrNotifi();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        ButterKnife.inject(this);
        Intent intent = getIntent();
        //获得传递过来的对象
        // contactBean = (ContactBean) intent.getSerializableExtra("contactBean");
        // String nick = contactBean.nick;
        //获取聊天对象的accout=session_id
//        session_id = contactBean.account;

        account = intent.getStringExtra("account");
        toAccount = account;
        session_id = account;
        String nick = intent.getStringExtra("nick");
        tvChatTitle.setText("正在与<" + nick + ">聊天");
        //发送消息的点击
        etSubmit.setOnClickListener(this);
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (chat == null) {
                    //获得chat管理器
                    cm1 = MyApp.conn.getChatManager();
                    //获得chat对象
                    chat = cm1.createChat(account, mlis);
                }
            }
        }.start();
        //添加聊天消息的内容观察者
        getContentResolver().registerContentObserver(TypeData.SMS_URI, true, mMyObserver);
        //刷新数据
        setAdapterOrNotifi();

    }

    //发送消息的点击
    @Override
    public void onClick(View v) {
        final String message = etChat.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        //访问网络操作 子线程
        new Thread() {
            @Override
            public void run() {
                super.run();
                //因为访问网络 应该在子线程
                try {
                    Message m = new Message();
                    m.setType(Message.Type.chat);
                    m.setFrom(MyApp.account);
                    m.setTo(toAccount);
                    m.setBody(message);
                    MessageDao md = new MessageDao(ChatActivity.this);
                    md.saveSendMessage(m);
                    chat.sendMessage(m);
                    //因为要更行ui 放在ui线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAdapterOrNotifi();
                            //将输入框置为空
                            etChat.setText("");
                        }
                    });

                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //监听消息的到来监听器
    MessageListener mlis = new MessageListener() {
        //接收消息
        @Override
        public void processMessage(Chat chat, final Message message) {
            //如果是消息类型
            if (Message.Type.chat.equals(message.getType())) {

                MessageDao md = new MessageDao(ChatActivity.this);
                //当有消息来到是 保存消息到本地数据库
                md.saveReceMessage(message);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(chat!=null){
//            chat.removeMessageListener(mlis);
//        }
    }

    //更新数据
    private void updata() {
        SmsDao s = new SmsDao(this);
        //找到所有聊天数据
        ArrayList<SMSBean> smsAll = s.findChatAll();
        //本次聊天的消息集合
        session = new ArrayList<>();
        for (SMSBean temp : smsAll) {
            //从所有消息中筛选出本次的会话所有消息
            String session_id_all = temp.SESSION_ID;
            //如果和本次聊天的session_id相同 则存进本次的集合
            if (session_id_all.equals(session_id)) {
                session.add(temp);
            }
        }
    }

    //设置和刷新适配器
    private void setAdapterOrNotifi() {
        //更新数据
        updata();
        //如果数据集合为空 不设置
        if (session.size() < 1) {
            return;
        }
        //如果适配器为空
        if (chatAda == null) {
            chatAda = new ChatAdapter();
            lvChat.setAdapter(chatAda);
            //跳至最后一条消息
            lvChat.setSelection(session.size() - 1);
        } else {//如果适配器不为空 则刷新适配器
            chatAda.notifyDataSetChanged();
            lvChat.setSelection(session.size() - 1);
        }
    }

    public class ChatAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return session.size();
        }

        @Override
        public Object getItem(int position) {
            return session.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SMSBean smsBean = session.get(position);
            //自己的ID
            String myID = MyApp.account;
            //消息类型 判断是自己的 还是对方的
            String from_id = smsBean.FROM_ID;
            View view;
            TextView tv;
            TextView showTime;
            if (!from_id.equals(myID)) {
                //左聊天框
                view = View.inflate(ChatActivity.this, R.layout.chat_left_item, null);
                tv = (TextView) view.findViewById(R.id.tv_left_item);
                showTime = (TextView) view.findViewById(R.id.tv_left_time);
            } else {
                //右边聊天框
                view = View.inflate(ChatActivity.this, R.layout.chat_right_item, null);
                tv = (TextView) view.findViewById(R.id.tv_right_item);
                showTime = (TextView) view.findViewById(R.id.tv_right_time);
            }
            //设置信息

            tv.setText(smsBean.BODY);
//            String format1 = StringUtils.format(ChatActivity.this, tv, smsBean.BODY);
//            System.out.println(format1);
//            tv.setText(format1);


            long time = smsBean.TIME;
            //格式化时间
            String format = TimeFormater.format(time);
            showTime.setText(format);

            return view;
        }
    }
}
