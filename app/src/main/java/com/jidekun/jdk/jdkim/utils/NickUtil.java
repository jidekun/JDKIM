package com.jidekun.jdk.jdkim.utils;

import android.content.Context;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

import com.jidekun.jdk.jdkim.bean.MyApp;

public class NickUtil {

	public static String getNick(String nick) {
		//汉字转拼音,转大写
		// PinyinHelper.convertToPinyinString(汉字, 符号, 格式);ge shi
		return PinyinHelper.convertToPinyinString(nick, "", PinyinFormat.WITHOUT_TONE).toUpperCase();
	}

	//老王@jdk-pc.com/Sparck 2.63 过滤掉后面Sparck
	public static String filterAccount(String account) {
		account=	account.substring(0,account.indexOf("@"));
		account=account+"@"+ MyApp.SERVER_NAME;
		return account;
	}

	public static String getNick(Context context, String account) {
		String nick = "";


//		Cursor c = context.getContentResolver().query(ContactProvider.CONTACT_URI, null, CONTACT.ACCOUNT + "=?", new String[] { account }, null);
//
//		if (c.moveToFirst()) {
//			nick = c.getString(c.getColumnIndex(CONTACT.NICK));
//		}
//		c.close();

		if (nick == null || "".equals(nick)) {
			nick = account.substring(0, account.indexOf("@"));
		}
		return nick;
	}
}
