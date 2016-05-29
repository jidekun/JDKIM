package com.jidekun.jdk.jdkim.activity;


import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jidekun.jdk.jdkim.R;
import com.jidekun.jdk.jdkim.bean.ContactBean;
import com.jidekun.jdk.jdkim.bean.TypeData;
import com.jidekun.jdk.jdkim.db.ContactDao;
import com.jidekun.jdk.jdkim.fragment.ContactFragment;
import com.jidekun.jdk.jdkim.fragment.SessionFragment;
import com.jidekun.jdk.jdkim.provider.ContactProvider;
import com.jidekun.jdk.jdkim.service.IMService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.rb_huihua)
    RadioButton rbHuihua;
    @InjectView(R.id.rb_haoyou)
    RadioButton rbHaoyou;
    @InjectView(R.id.rg)
    RadioGroup rg;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.vp_main)
    ViewPager vpMain;
    private List<Fragment> fragmentList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        //设置viewgroup的点击事件
        rg.setOnCheckedChangeListener(this);
        fragmentList = new ArrayList<>();
        //将fragment添加到集合中
        fragmentList.add(new SessionFragment());
        fragmentList.add(new ContactFragment());
        //创建在viewpager中使用fragment的适配器对象, 构造方法传入fragment管理器
        FragmentAdapetr fragmentAdapetr = new FragmentAdapetr(getSupportFragmentManager());
        //设置适配器
        vpMain.setAdapter(fragmentAdapetr);
        //设置页面变化监听
        vpMain.addOnPageChangeListener(this);
        //默认选中的页面
        vpMain.setCurrentItem(1);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //切换页面同时切换radiogroup
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                rg.check(R.id.rb_huihua);
                break;
            case 1:
                rg.check(R.id.rb_haoyou);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_haoyou:
                tvTitle.setText("好友界面");
                vpMain.setCurrentItem(1);
                break;
            case R.id.rb_huihua:
                tvTitle.setText("会话界面");
                vpMain.setCurrentItem(0);
                break;
        }
    }

    class FragmentAdapetr extends FragmentPagerAdapter {
        public FragmentAdapetr(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当activity被销毁时,停止服务
        stopService(new Intent(MainActivity.this, IMService.class));

    }
}
