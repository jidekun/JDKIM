package com.jidekun.jdk.jdkim.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jidekun.jdk.jdkim.R;
import com.jidekun.jdk.jdkim.bean.MyApp;
import com.jidekun.jdk.jdkim.provider.ContactProvider;
import com.jidekun.jdk.jdkim.service.ChatService;
import com.jidekun.jdk.jdkim.service.IMService;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends Activity {

    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.editText2)
    EditText editText2;
    @InjectView(R.id.editText)
    EditText editText;
    @InjectView(R.id.button)
    Button button;
    private String userName;
    private String passWord;
    //链接对象
    private XMPPConnection conn;
    //定义flag 判断是否登陆成功
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        new Thread() {
            @Override
            public void run() {

                try {
                    //配置服务器端口 ip
                    ConnectionConfiguration connection = new ConnectionConfiguration(MyApp.HOST, MyApp.PORT);
                    //设置debug信息  打印debug
                    connection.setDebuggerEnabled(true);
                    //使用明文传输
                    connection.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                    //创建通道
                    conn = new XMPPConnection(connection);
                    conn.connect();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editText2.getText().toString().trim();
                passWord = editText.getText().toString().trim();
                //登陆属于网络操作 在子线程
                new Thread() {
                    @Override
                    public void run() {

                        try {
                            //执行登陆操作
                            conn.login(userName, passWord);
                            //登陆成功 flag=true
                            flag = true;
                            //开ui线程
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //登陆成功  跳转页面 保存通道
                                    if (flag) {
                                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                        //跳转主界面
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        //开启 监听联系人变动服务
                                        startService(new Intent(LoginActivity.this, IMService.class));
                                        //开启监听消息的服务
                                        startService(new Intent(LoginActivity.this, ChatService.class));
                                        //保存通道
                                        MyApp.conn = conn;
                                        //保存账号
                                        MyApp.username = userName;
                                        MyApp.account = userName+"@jdk-pc";
                                        //关闭当前页面
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (XMPPException e) {
                            e.printStackTrace();
                            flag = false;
                        }
                    }
                }.start();

            }
        });
    }
}
