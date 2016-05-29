package com.jidekun.jdk.jdkim.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.jidekun.jdk.jdkim.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(500);
                Intent intent =new Intent();
                intent.setClass(SplashActivity.this,LoginActivity.class);
                SplashActivity.this.startActivity(intent);
                finish();
            }
        }.start();
    }
}
