package com.fafu.kongshu.zhengxianyou.pinke;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.fafu.kongshu.zhengxianyou.pinke.fragment.Login_Fragment;

/**
 * Fragmemt的宿主Activity
 * 包含：Login_Fragment，Register_Fragment，Forget_Fragment三个Fragment;
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkNeedPermission();
        init();
    }

    private void checkNeedPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE
            },1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
            },2);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            },3);
        }
    }

    /**
     * 把Fragment放进宿主Activity；
     */
    private void init() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_ring_up_container,new Login_Fragment())
                .commit();
    }
}
