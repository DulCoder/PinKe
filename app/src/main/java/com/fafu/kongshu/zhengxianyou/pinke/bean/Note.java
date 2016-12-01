package com.fafu.kongshu.zhengxianyou.pinke.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhengxianyou on 2016/10/23.
 */

public class Note extends BmobObject {
    private String title;                     //标题
    private String noteId;                    //id
    private String origin;                    //起点   /start
    private String destination;               //目的地 /end
    private String time;                      //出发时间
    private String datetime;                  //发布时时间
    private String content;                   //内容
    private MyUser author;                    //作者
    private String phoneNumber;               //手机号码
    private String currentLocation;           //当前位置
    private String myIcon;                    //头像
    private String nickName;                  //昵称
    private Double mLatitude;                 //纬度
    private Double mLongitude;                //经度

    public Note() {
    }

    public Note(String title, String origin, String destination, String time, String datetime, String content, String phoneNumber, String currentLocation, String myIcon, String nickName) {
        this.title = title;
        this.origin = origin;
        this.destination = destination;
        this.time = time;
        this.datetime = datetime;
        this.content = content;
        this.phoneNumber = phoneNumber;
        this.currentLocation = currentLocation;
        this.myIcon = myIcon;
        this.nickName = nickName;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMyIcon() {
        return myIcon;
    }

    public void setMyIcon(String myIcon) {
        this.myIcon = myIcon;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
