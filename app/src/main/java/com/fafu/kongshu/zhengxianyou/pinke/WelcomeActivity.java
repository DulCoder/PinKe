package com.fafu.kongshu.zhengxianyou.pinke;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

/**
 * 欢迎界面，程序进入欢迎界面后会首先判断程序是否是第一次运行，
 * 如果是则在两秒后进入引导页面，否则进入登录界面
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final int TIME = 2000;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private boolean isFirstIn = false;

    /**
     * Handler用来设置延时时间
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;

                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
    }


    private void init() {
        SharedPreferences sp = getSharedPreferences("Demo", MODE_PRIVATE);
        isFirstIn = sp.getBoolean("isFirstIn", true);

        if (!isFirstIn) {
            mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, TIME);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirstIn", false);
            editor.commit();
        }
    }

    //进入登录界面
    private void goHome() {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }

    //进入引导页
    private void goGuide() {
        startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
        finish();
    }

}
