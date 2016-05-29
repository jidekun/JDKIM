package com.jidekun.jdk.jdkim.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by JDK on 2016/5/26.
 */
public class StringUtils {
    @Nullable
    public static String format(Context context, TextView tv, String msg) {
        String message = msg;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        //获取当前屏幕宽高 单位是像素
        int width = dm.widthPixels;
        //文字的size
        int textSize = 20;
        tv.setTextSize(textSize);
        //获得屏幕百分之百时的宽度
        int textviewWidth = (int) (width * 0.8);
        //获得textview每行显示字的个数
        int lineCount = textviewWidth / textSize - 1;
        //字符串的字符个数
        int stringLength = message.length();
        //获得要显示多少行
        int count = stringLength / lineCount;
        String temp = "";
        for (int i = 0; i < count - 1; i++) {
            String substring = msg.substring(lineCount * i, lineCount * (i + 1));
            //加上换行符号
            if (i == count - 1) {
                temp += substring;
            } else {
                temp += substring + "\n";
            }
        }
        return temp;
    }
}
