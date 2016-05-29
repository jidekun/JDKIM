package com.jidekun.jdk.jdkim.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JDK on 2016/5/24.
 */
public abstract class BaseFragment extends Fragment {
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //判断是否为空,不重新创建对象,保证fragment上的数据不丢失
        if (view == null) {
            view = createView(inflater, container, savedInstanceState);
        } else {
            //获得父控件
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            //如果父控件不为空 则先删除父控件上的子控件 ,不然会出现bug
            if (viewGroup != null) {
                viewGroup.removeAllViews();
            }
        }
        return view;
    }

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
