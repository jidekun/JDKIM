package com.jidekun.jdk.jdkim.db.openhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JDK on 2016/5/25.
 */
public class ContactOpen extends SQLiteOpenHelper {

    public ContactOpen(Context context) {
        super(context, "contact.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table contacts(_id integer primary key autoincrement,account  varchar(20),nick  varchar(20),avatar integer,sort  varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
