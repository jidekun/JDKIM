package com.jidekun.jdk.jdkim.fragment;


import android.content.Intent;
import android.database.ContentObserver;
import android.net.PskKeyManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jidekun.jdk.jdkim.R;
import com.jidekun.jdk.jdkim.activity.ChatActivity;
import com.jidekun.jdk.jdkim.base.BaseFragment;
import com.jidekun.jdk.jdkim.bean.ContactBean;
import com.jidekun.jdk.jdkim.bean.MyApp;
import com.jidekun.jdk.jdkim.bean.SMSBean;
import com.jidekun.jdk.jdkim.db.SmsDao;
import com.jidekun.jdk.jdkim.utils.NickUtil;

import java.util.ArrayList;
import java.util.HashSet;

public class SessionFragment extends BaseFragment implements ListView.OnItemClickListener {


    private ListView lv;
    //监听聊天的创建
    private MyObserver mMyObserver = new MyObserver(new Handler());
    private ArrayList<String> session;
    private SessionAdapter sessionAda;


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
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, container, false);
        lv = (ListView) view.findViewById(R.id.lv_session);
        lv.setOnItemClickListener(this);
        setAdapterOrNotifi();

        return view;
    }

    //listview条目点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String account = session.get(position);
        Intent intent = new Intent();
        intent.setClass(getActivity(), ChatActivity.class);
        intent.putExtra("account", account);
        String nick = NickUtil.getNick(getActivity(), account);
        intent.putExtra("nick", nick);
        getActivity().startActivity(intent);
    }

    //更新数据
    private void updata() {
        SmsDao s = new SmsDao(getActivity());
        //找到所有聊天数据
        ArrayList<SMSBean> smsAll = s.findChatAll();
        //所有会话的集合
        session = new ArrayList<>();
        for (SMSBean temp : smsAll) {
            //获取所有消息的会话id
            String account = temp.SESSION_ID;
            session.add(account);
        }
        //除重处理
        HashSet<String> hs =new HashSet<>();
        hs.addAll(session);
        session.clear();
        session.addAll(hs);
        hs=null;
        System.out.println(session.size());
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
        if (sessionAda == null) {
            sessionAda = new SessionAdapter();
            lv.setAdapter(sessionAda);
        } else {//如果适配器不为空 则刷新适配器
            sessionAda.notifyDataSetChanged();
        }
    }

    public class SessionAdapter extends BaseAdapter {
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
            //聊天对象名字
            TextView nick = null;
            //对方最后一条聊天内容
            TextView lastBody = null;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.qqhaoyoulist_item, null);
                nick = (TextView) convertView.findViewById(R.id.nick);
                lastBody = (TextView) convertView.findViewById(R.id.account);
            }
            String account = session.get(position);
            String nick1 = NickUtil.getNick(getActivity(), account);
            nick.setText(nick1);
            return convertView;
        }
    }
}
