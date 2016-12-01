package com.fafu.kongshu.zhengxianyou.pinke;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;
import com.fafu.kongshu.zhengxianyou.pinke.fragment.DisplayFragment;
import com.fafu.kongshu.zhengxianyou.pinke.fragment.MapFragment;
import com.fafu.kongshu.zhengxianyou.pinke.fragment.MyContentFragment;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_all, btn_map, btn_mine;
    public static LinearLayout ll_btn;
    private DisplayFragment mDisplayFragment = DisplayFragment.newInstance();
    private MyContentFragment mContentFragment = MyContentFragment.newInstance();

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    double latitude = amapLocation.getLatitude();
                    double longitude = amapLocation.getLongitude();
                    String tempLocation = amapLocation.getCity() + amapLocation.getDistrict() + amapLocation.getStreet();
                    Config.setLatitude(latitude);
                    Config.setLongitude(longitude);
                    Config.setTempLocation(tempLocation);
                    Log.e("GAODE", tempLocation);
                    //可在其中解析amapLocation获取相应内容。
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    /**
     * 接受fragment传递过来的用于更改UI的信息
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setLlVisibility();                                     //隐藏导航栏
                    break;
                case 1:
                    ll_btn.setVisibility(View.VISIBLE);                    //显示 导航栏
                    break;
                case 2:
                    btn_all.setTextColor(0xffff4081);
                    btn_mine.setTextColor(0xffffffff);
                    btn_map.setTextColor(0xffffffff);
                    break;
                case 3:
                    btn_mine.setTextColor(0xffFF4081);
                    btn_all.setTextColor(0xffffffff);
                    btn_map.setTextColor(0xffffffff);
                    break;
                case 4:
                    btn_map.setTextColor(0xffFF4081);
                    btn_all.setTextColor(0xffffffff);
                    btn_mine.setTextColor(0xffffffff);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Config.setIsRefresh(true);                   //设置需要刷新
        Config.setIsDisplayFragmentAlive(true);      //设置DisplayFragment的生命状态为激活
        Config.setIsMyContentFragmentAlive(false);   //设置MyContentFragment的生命状态为未激活
        Config.setIsMapFragmentAlive(false);         //设置MapFragment的生命状态为未激活

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置退出时是否杀死service
        mLocationOption.setKillProcess(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取是否单次定位
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        String name = getIntent().getStringExtra("name");
        String icon = getIntent().getStringExtra("icon");
        String nickName = getIntent().getStringExtra("nickName");
        Config.setName(name);
        Config.setMyIcon(icon);
        Config.setNickName(nickName);



        addFragment();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 填充Fragment
     */
    private void addFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mDisplayFragment, null)
                .commit();
    }

    /**
     * 初始化View
     */
    private void initView() {
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);

        btn_all = (Button) findViewById(R.id.btn_all);
        btn_all.setOnClickListener(this);

        btn_map = (Button) findViewById(R.id.btn_map);
        btn_map.setOnClickListener(this);

        btn_mine = (Button) findViewById(R.id.btn_mine);
        btn_mine.setOnClickListener(this);

    }

    //隐藏导航栏
    public static void setLlVisibility() {
        ll_btn.setVisibility(View.GONE);

    }

    /**
     * 点击事件相关逻辑
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all:
                addDisplayFragment();
                break;

            case R.id.btn_map:
                addMapFragment();
                break;

            case R.id.btn_mine:
                addMyContentFragment();
                break;
        }
    }

    /**
     * 跳转到地图模式界面
     */
    private void addMapFragment() {
        if (!Config.isMapFragmentAlive()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, MapFragment.newInstance(), null)
//                .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * 首界面
     */
    private void addDisplayFragment() {
        if (!Config.isDisplayFragmentAlive()) {       //如果当前界面就是DisplayFragment则点击无效否则有效
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DisplayFragment(), null)
                    .commit();
        }
    }

    /**
     * 我的发布界面
     */
    private void addMyContentFragment() {
        if (!Config.isMyContentFragmentAlive()) {     //仅当DisplayFragment存活才可跳转
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mContentFragment, null)
                    .commit();

        }
    }

    /**
     * 接受fragment的信息，更改UI
     */
    public void setHandler(int i) {
        Message msg = handler.obtainMessage();
        msg.what = i;
        handler.sendMessage(msg);
    }

}
