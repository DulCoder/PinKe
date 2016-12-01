package com.fafu.kongshu.zhengxianyou.pinke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.fafu.kongshu.zhengxianyou.pinke.DisplayActivity;
import com.fafu.kongshu.zhengxianyou.pinke.R;
import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;

import java.util.List;

/**
 * Created by zhengxianyou on 2016/11/12.
 */

public class MapFragment extends Fragment {
    private View mView;
    private TextureMapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LatLng amapLocation = new LatLng(Config.getLatitude(), Config.getLongitude());


    private View pop;
    private TextView tv;
    private DisplayActivity mDisplayActivity;

    /**
     * 返回创建fragment实例
     */
    public static MapFragment newInstance() {
        MapFragment mFragment = new MapFragment();
        return mFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDisplayActivity = (DisplayActivity) context;
        Log.e("test", "map onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        Config.setIsMapFragmentAlive(true);
        mDisplayActivity.setHandler(4);

          //坐标转换
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        converter.coord(amapLocation);
        LatLng bmapLocation = converter.convert();

        //获取地图控件引用
        mMapView = (TextureMapView) mView.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //设置地图中心点及地图缩放大小，数值越大现实的范围越小越详细，默认为13(2公里)
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(bmapLocation, 13);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        Log.e("test", "map onCreateView");


        initMarker();
        initListener();

        return mView;
    }

    /**
     * 初始化Marker
     */
    private void initMarker() {

        List<Note> mList = Config.getList();
        int k = mList.size();
        Log.e("test", String.valueOf(k));

//        Iterator<Note> ite = mList.iterator();
//        while (ite.hasNext()) {
        for (int i = 0; i < k; i++) {
            MarkerOptions option = new MarkerOptions();

            Note note = mList.get(i);
            Double latitude = note.getLatitude();
            Double longitude = note.getLongitude();
            LatLng location = new LatLng(latitude, longitude);
            String start = note.getOrigin();
            String end = note.getDestination();
            String time = note.getTime();

            option.position(location)                                                  //设置位置
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))   //设置图标
                    .title(start + "→" + end + ":" + time + "出发")                                 //设置标题
                    .draggable(false);                                                 //设置可以拖动
            mBaiduMap.addOverlay(option);
        }

    }

    /**
     * 初始Marker化监听事件
     */
    private void initListener() {
        //点击事件
        mBaiduMap.setOnMarkerClickListener(clickListener);

    }

    /**
     * Marker点击事件
     */
    BaiduMap.OnMarkerClickListener clickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            /* 显示一个气泡*/
            if (pop == null) {
                pop = View.inflate(getContext(), R.layout.pop, null);
                tv = (TextView) pop.findViewById(R.id.tv_title);
                mMapView.addView(pop, getParams(marker));              //添加气泡
            } else {
                mMapView.updateViewLayout(pop, getParams(marker));
            }
            tv.setText(marker.getTitle());
            return true;
        }
    };


    /**
     * 获取到MapViewLayoutParams的对象params
     */
    public MapViewLayoutParams getParams(Marker marker) {
        LatLng position = marker.getPosition();                        //获取marker的位置
        MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode);   //指定坐标类型为经纬度
        builder.position(position);                                    //设置气泡位置
        builder.yOffset(-75);                                          //设置气泡往上偏移
        MapViewLayoutParams params = builder.build();
        return params;
    }

    @Override
    public void onResume() {
        super.onResume();
        Config.setIsMapFragmentAlive(true);                   //设置MapFragment的状态为激活
        Log.e("test", "map resume");
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("test", "map onDestroy");
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        Config.setIsMapFragmentAlive(false);                  //设置MapFragment的状态为未激活
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("test", "map onPause");
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("test", "map onDestroyView");

    }
}
