package com.jidekun.jdk.jdkim.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jidekun.jdk.jdkim.bean.SMSBean;
import com.jidekun.jdk.jdkim.bean.TypeData;
import com.jidekun.jdk.jdkim.db.openhelper.SmsOpen;

import java.util.ArrayList;

/**
 * Created by JDK on 2016/5/26.
 */
public class SmsDao {
    private Context context;
    private SmsOpen smsOpen;

    public static final String TABLE = "sms";
    private SQLiteDatabase db;

    public SmsDao(Context context) {
        this.context = context;
        smsOpen = new SmsOpen(context);
    }

    public void add(ContentValues values) {
        System.out.println("执行了数据添加");
        db = smsOpen.getWritableDatabase();
        //返回值为插入数据库表的第几行
        long insert = db.insert(TABLE, null, values);
        context.getContentResolver().notifyChange(TypeData.SMS_URI, null);
        db.close();//关闭数据库释放资源
    }

    public ArrayList<SMSBean> findChatAll() {
        SMSBean smsBean;
        ArrayList<SMSBean> list = new ArrayList<>();
        db = smsOpen.getWritableDatabase();
        //按照时间排序
        Cursor cursor = db.query(TABLE, null, null, null, null, null, "time ASC");
        while (cursor.moveToNext()) {
            smsBean = new SMSBean();
            /**
             * _id integer
             * session_id
             * session_name
             * from_id
             * from_nick
             * from_avatar
             * body
             * type
             * unread integer,
             * status text,
             * time long
             */
            smsBean.SESSION_ID = cursor.getString(1);
            smsBean.SESSION_NAME = cursor.getString(2);
            smsBean.FROM_ID = cursor.getString(3);
            smsBean.FROM_NICK = cursor.getString(4);
            smsBean.BODY = cursor.getString(6);
            smsBean.TIME = cursor.getLong(10);
            list.add(smsBean);
        }
        cursor.close();
        db.close();
        return list;
    }

}
