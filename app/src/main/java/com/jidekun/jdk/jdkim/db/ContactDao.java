package com.jidekun.jdk.jdkim.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jidekun.jdk.jdkim.bean.ContactBean;
import com.jidekun.jdk.jdkim.bean.TypeData;
import com.jidekun.jdk.jdkim.db.openhelper.ContactOpen;

import java.util.ArrayList;

/**
 * Created by JDK on 2016/5/25.
 */
public class ContactDao {
    ContactOpen contactOpen;
    Context context;
    static final String TABLE = "contacts";

    public ContactDao(Context context) {
        this.context = context;
        contactOpen = new ContactOpen(context);
    }

    //开启事务
/*
*    db.beginTransaction();
    try
    {

        //设置事务标志为成功，当结束事务时就会提交事务
        db.setTransactionSuccessful();
    }
    finally
    {
        //结束事务
        db.endTransaction();
    }
}
* */
    private SQLiteDatabase db;

    public void add(ContentValues values) {
        db = contactOpen.getWritableDatabase();
        //返回值为插入数据库表的第几行
        long insert = db.insert(TABLE, null, values);
        context.getContentResolver().notifyChange(TypeData.CONTACT_URI, null);
        db.close();//关闭数据库释放资源

    }

    public boolean delete(String account) {
        db = contactOpen.getWritableDatabase();
        int result = db.delete(TABLE, "account=?", new String[]{account});
        db.close();
        context.getContentResolver().notifyChange(TypeData.CONTACT_URI, null);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateMode(String account, ContentValues values) {

        db = contactOpen.getWritableDatabase();
        int result = db.update(TABLE, values, "account=?", new String[]{account});
        db.close();
        context.getContentResolver().notifyChange(TypeData.CONTACT_URI, null);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ContactBean find(String account) {
        ContactBean contactBean = null;
        db = contactOpen.getWritableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{"nick,avatar,sort"}, "account=?", new String[]{account}, null, null, null);
        if (cursor.moveToNext()) {
            contactBean = new ContactBean();
            String nick = cursor.getString(0);
            int avatar = cursor.getInt(1);
            String sort = cursor.getString(2);
            contactBean.account = account;
            contactBean.nick = nick;
            contactBean.avatar = avatar;
            contactBean.sort = sort;
        }
        cursor.close();
        db.close();
        return contactBean;
    }

    public Cursor cursor() {
        ContactBean contactBean = null;
        SQLiteDatabase db = contactOpen.getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        cursor.close();
        db.close();
        return cursor;
    }

    public ArrayList<ContactBean> findAll() {
        ContactBean contactBean;
        ArrayList<ContactBean> list = new ArrayList<>();
        SQLiteDatabase db = contactOpen.getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, TypeData.SORT + " ASC");
        while (cursor.moveToNext()) {
            contactBean = new ContactBean();
            String account = cursor.getString(1);
            String nick = cursor.getString(2);
            int avatar = cursor.getInt(3);
            String sort = cursor.getString(4);
            contactBean.account = account;
            contactBean.nick = nick;
            contactBean.avatar = avatar;
            contactBean.sort = sort;
            list.add(contactBean);
        }
        cursor.close();
        db.close();
        return list;
    }
}
