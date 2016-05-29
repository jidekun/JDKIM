package com.jidekun.jdk.jdkim.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;


public class ContactProvider extends ContentProvider {
    public ContactProvider() {
    }

    //将访问内容提供者的名称,避免硬编码
    private static final String AUTHORITY = "com.jidekun.jdk.jdkim.provider.ContactProvider";
    public static final String TABLE = "contacts";
    //访问该contentprovider的uri地址
    public static final Uri CONTACT_URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE);
    //urimatcher 匹配是返回值 避免硬编码
    public static final int SUCCESS = 1;
    //定义一个uri匹配器
    static UriMatcher uriMatcher ;
    //数据库名
    private static final String DB = "contact.db";
    //数据库表名

    //Sql语句 建表语句
    private static final String SQL = "create table contacts(_id integer primary key autoincrement,account text,nick text,avatar integer,sort Text)";
    private ContactOnpenHelper contactOnpenHelper;
    private SQLiteDatabase db;

    //添加一条规则
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE, SUCCESS);
    }
    // 表结构
    // BaseColumns _id
    public static class CONTACT implements BaseColumns {
        public static final String ACCOUNT = "account";
        public static final String NICK = "nick";
        public static final String AVATAR = "avatar";
        public static final String SORT = "sort";// 拼音
    }
    @Override
    public boolean onCreate() {
        //初始化帮助类
        contactOnpenHelper = new ContactOnpenHelper(getContext());
        return (contactOnpenHelper == null) ? false : true;
    }

    //增
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case SUCCESS:
                db = contactOnpenHelper.getWritableDatabase();
                //返回值判断是否插入成功
                long id = db.insert(TABLE, "", values);
                if (id != -1) {
                    uri = ContentUris.withAppendedId(uri, id);
                }
                break;
        }
        return uri;
    }

    //删
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case SUCCESS:
                db = contactOnpenHelper.getWritableDatabase();
                count = db.delete(TABLE, selection, selectionArgs);
                break;
        }
        //  返回删除的记录数
        return count;
    }

    //改
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case SUCCESS:
                db = contactOnpenHelper.getWritableDatabase();
                count = db.update(TABLE, values, selection, selectionArgs);
                break;
        }
        return count;
    }

    //查
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case SUCCESS:
                db = contactOnpenHelper.getWritableDatabase();
                cursor = db.query(TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    //创建帮助类
    private class ContactOnpenHelper extends SQLiteOpenHelper {

        public ContactOnpenHelper(Context context) {
            super(context, DB, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
