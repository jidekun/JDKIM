package com.jidekun.jdk.jdkim.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.jidekun.jdk.jdkim.bean.MyApp;
import com.jidekun.jdk.jdkim.bean.TypeData;
import com.jidekun.jdk.jdkim.db.ContactDao;
import com.jidekun.jdk.jdkim.utils.NickUtil;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;


public class IMService extends Service {

    private Roster roster;

    public IMService() {
    }

    //联系人的监听器
    private RosterListener rosterListener = new RosterListener() {


        //                //直接打印是克林顿
//                System.out.println(temp);
//                //getName 克林顿 getUser 克林顿@jdk-pc
//                System.out.println("getName" + person.getName() + "getUser" + person.getUser() + "getStatus" + person.getStatus());
        //当添加
        @Override
        public void entriesAdded(Collection<String> collection) {
            RosterEntry person;
            for (String temp : collection) {
                person = roster.getEntry(temp);
                saveOrUpdateRosterEntry(person);
            }
        }

        //当更新
        @Override
        public void entriesUpdated(Collection<String> collection) {
            RosterEntry person1;
            for (String temp : collection) {
                for (String temp1 : collection) {
                    person1 = roster.getEntry(temp1);
                    saveOrUpdateRosterEntry(person1);
                }
            }
        }

        //当删除
        @Override
        public void entriesDeleted(Collection<String> collection) {
            for (String temp : collection) {
                //川普@jdk-pc
                ContactDao dao = new ContactDao(getBaseContext());
                boolean delete = dao.delete(temp );
                if (delete) {
                    //System.out.println("删除成功");
                } else {
                   // System.out.println("删除失败");
                }
            }
        }

        //上离线
        @Override
        public void presenceChanged(Presence presence) {
            System.out.println(presence.toXML());
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("imservice服务开启");
        roster = MyApp.conn.getRoster();
        //添加监听器
        roster.addRosterListener(rosterListener);
        //获得所有联系人的集合
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry temp : entries) {
            saveOrUpdateRosterEntry(temp);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除监听器
        roster.removeRosterListener(rosterListener);
        System.out.println("im服务被关闭");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //保存或者更新数据, 有数据则更新 没数据则保存
    public void saveOrUpdateRosterEntry(RosterEntry person) {
        ContentValues values = new ContentValues();
        String account = person.getUser();// 老王@jdk-pc
        values.put(TypeData.ACCOUNT, person.getUser());
        String nick = person.getName();
        //如果昵称为空或者null 则切割用户名前面的字符串当作昵称
        if (nick == null || "".equals(nick)) {
            nick = account.substring(0, account.indexOf("@"));
        }
        values.put(TypeData.NICK, nick);
        values.put(TypeData.AVATAR, 0);
        //通过工具将汉字转换成拼音
        values.put(TypeData.SORT, NickUtil.getNick(nick));
        ContactDao contactDao = new ContactDao(getBaseContext());
        //先更新 如果没有对应的键 则向数据库插入
        //更新成功 返回true 否则 添加该数据
        boolean count = contactDao.updateMode(account, values);
        if (!count) {
            contactDao.add(values);
        }
    }
}

