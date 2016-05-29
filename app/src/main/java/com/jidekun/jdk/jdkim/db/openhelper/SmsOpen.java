package com.jidekun.jdk.jdkim.db.openhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JDK on 2016/5/26.
 */
public class SmsOpen extends SQLiteOpenHelper {
    public static final String DB = "sms.db";
    public SmsOpen(Context context) {
        super(context, DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table sms (_id integer  primary key autoincrement,session_id text,session_name text,from_id text,from_nick text,from_avatar integer ,body text,type text ,unread integer,status text,time long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
