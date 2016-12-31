package com.fafu.kongshu.zhengxianyou.pinke.config;

import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengxianyou on 2016/11/1.
 * 全局可获取的数据信息
 */

public class Config {
    private static boolean isDisplayFragmentAlive ;                  //判断DisplayFragment是否存活
    private static boolean isMyContentFragmentAlive;                 //判断MyContentFragment是否存活
    private static boolean isMapFragmentAlive;                       //判断MapFragment是否存活
    private static boolean isRefresh;                                //判断是否需要刷新
    private static String name;                                       //保存用户名
    private static String nickName;                                  //保存昵称
    private static String myIcon;                                    //保存头像信息
    private static String tempLocation;                              //临时存储位置
    private static Double latitude;                                  //当前纬度
    private static Double longitude;                                 //当前经度
    private static List<Note> list = new ArrayList<>();              //保存显示加载的note数据

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Config.name = name;
    }

    public static boolean isMapFragmentAlive() {
        return isMapFragmentAlive;
    }

    public static void setIsMapFragmentAlive(boolean isMapFragmentAlive) {
        Config.isMapFragmentAlive = isMapFragmentAlive;
    }

    public static boolean isRefresh() {
        return isRefresh;
    }

    public static void setIsRefresh(boolean isRefresh) {
        Config.isRefresh = isRefresh;
    }

    public static Double getLatitude() {
        return latitude;
    }

    public static void setLatitude(Double latitude) {
        Config.latitude = latitude;
    }

    public static Double getLongitude() {
        return longitude;
    }

    public static void setLongitude(Double longitude) {
        Config.longitude = longitude;
    }

    public static List<Note> getList() {
        return list;
    }

    public static void setList(List<Note> list) {
        Config.list = list;
    }

    public static String getTempLocation() {
        return tempLocation;
    }

    public static void setTempLocation(String tempLocation) {
        Config.tempLocation = tempLocation;
    }

    public static String getMyIcon() {
        return myIcon;
    }

    public static boolean isMyContentFragmentAlive() {
        return isMyContentFragmentAlive;
    }

    public static void setIsMyContentFragmentAlive(boolean isMyContentFragmentAlive) {
        Config.isMyContentFragmentAlive = isMyContentFragmentAlive;
    }

    public static void setMyIcon(String myIcon) {
        Config.myIcon = myIcon;
    }

    public static String getNickName() {
        return nickName;
    }

    public static void setNickName(String nickName) {
        Config.nickName = nickName;
    }

    public static boolean isDisplayFragmentAlive() {
        return isDisplayFragmentAlive;
    }

    public static void setIsDisplayFragmentAlive(boolean isDisplayFragmentAlive) {
        Config.isDisplayFragmentAlive = isDisplayFragmentAlive;
    }


}
