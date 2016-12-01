package com.fafu.kongshu.zhengxianyou.pinke;

import android.app.Application;
import android.content.SharedPreferences;

import com.amap.api.location.AMapLocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.fafu.kongshu.zhengxianyou.pinke.config.Constant;

import cn.bmob.v3.Bmob;

/**
 * Created by zhengxianyou on 2016/11/6.
 */

public class PinKeApplication extends Application{
    public static SharedPreferences  sp;
    public static  SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
         sp = getSharedPreferences(Constant.SP_NAME,MODE_PRIVATE);
         editor = sp.edit();
        Bmob.initialize(this, Constant.Bmob_APPID);
        SDKInitializer.initialize(getApplicationContext());
        AMapLocationClient.setApiKey(Constant.GAODE_KEY);

    }


}
