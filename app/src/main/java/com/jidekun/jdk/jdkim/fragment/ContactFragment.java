package com.jidekun.jdk.jdkim.fragment;


import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jidekun.jdk.jdkim.R;
import com.jidekun.jdk.jdkim.activity.ChatActivity;
import com.jidekun.jdk.jdkim.base.BaseFragment;
import com.jidekun.jdk.jdkim.bean.ContactBean;
import com.jidekun.jdk.jdkim.bean.TypeData;
import com.jidekun.jdk.jdkim.db.ContactDao;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ContactFragment extends BaseFragment implements ListView.OnItemClickListener {

    private ArrayList<ContactBean> list;
    private MyObserver mMyObserver = new MyObserver(new Handler());
    private ListView lv;
    private ContactAdapter contactAdapter;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactBean contactBean = list.get(position);
        Intent intent = new Intent();
//        intent.putExtra("contactBean", contactBean);
        intent.putExtra("account",contactBean.account);
        intent.putExtra("nick",contactBean.nick);
        intent.setClass(getActivity(), ChatActivity.class);
        getActivity().startActivity(intent);
    }

    private class MyObserver extends ContentObserver {

        public MyObserver(Handler handler) {
            super(handler);
        }


        @Override
        public void onChange(boolean selfChange, Uri uri) {
            //当数据库增删改时
            setAdaOrUpdate();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getContentResolver().registerContentObserver(TypeData.CONTACT_URI, true, mMyObserver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消内容观察者
        getActivity().getContentResolver().unregisterContentObserver(mMyObserver);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        lv = (ListView) view.findViewById(R.id.lv_contact);
        lv.setOnItemClickListener(this);
        //注册内容观察者
        setAdaOrUpdate();
        return view;
    }

    private void setAdaOrUpdate() {
        ContactDao d = new ContactDao(getActivity());
        list = d.findAll();

        if (contactAdapter == null) {
            if (list.size() > 0) {
                contactAdapter = new ContactAdapter();
                lv.setAdapter(contactAdapter);
            }
        } else {
            contactAdapter.notifyDataSetChanged();
        }

    }


    private class ContactAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.qqhaoyoulist_item, null);
                viewHolder = new ViewHolder(convertView);
                viewHolder.nick = (TextView) convertView.findViewById(R.id.nick);
                viewHolder.account = (TextView) convertView.findViewById(R.id.account);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.nick.setText(list.get(position).nick);
            viewHolder.account.setText(list.get(position).account);
            return convertView;
        }
    }

    static class ViewHolder {
        @InjectView(R.id.avatar)
        ImageView avatar;
        @InjectView(R.id.nick)
        TextView nick;
        @InjectView(R.id.account)
        TextView account;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
