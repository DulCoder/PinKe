package com.fafu.kongshu.zhengxianyou.pinke.sqlitedb;

import android.provider.BaseColumns;

/**
 * Created by zhengxianyou on 2016/11/28.
 */

public final class NoteMeteData {

//  private static String name = Config.getName();

    private NoteMeteData() {
    }

    public static class NativeUser implements BaseColumns{

        public static final String TABLE_NAME = "nativeUser";

        public static final String USERNAME = "userName";

        public static final String NICKNAME = "nickName";

    }

    public static class DisplayNoteTable implements BaseColumns {

        public static final String TABLE_NAME = "display";                              //表名

        public static final String TITLE = "title";                                     //标题

        public static final String ORIGIN = "origin";                                   //起点

        public static final String DESTINATION = "destination";                         //目的地

        public static final String TIME = "time";                                       //出发时间

        public static final String DATETIME = "datetime";                               //发布时时间

        public static final String CONTENT = "content";                                 //内容

        public static final String PHONENUMBER = "phoneNumber";                         //手机号码

        public static final String CURRENTLOCATION = "currentLocation";                 //当前位置

        public static final String MYICON = "myIcon";                                   //头像

        public static final String NICKNAME = "nickName";                               //昵称

    }

    public static class MyNoteTable implements BaseColumns {

        public static final String TABLE_NAME = "mine";                                 //表名

        public static final String TITLE = "title";                                     //标题

        public static final String ORIGIN = "origin";                                   //起点

        public static final String DESTINATION = "destination";                         //目的地

        public static final String TIME = "time";                                       //出发时间

        public static final String DATETIME = "datetime";                               //发布时时间

        public static final String CONTENT = "content";                                 //内容

        public static final String PHONENUMBER = "phoneNumber";                         //手机号码

        public static final String CURRENTLOCATION = "currentLocation";                 //当前位置

        public static final String MYICON = "myIcon";                                   //头像

        public static final String NICKNAME = "nickName";                               //昵称

        public static final String OBJECTID = "objectId";                               //objectId

//        public static final String USERNAME = "user"+name;                               //外键

    }
}
