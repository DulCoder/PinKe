package com.fafu.kongshu.zhengxianyou.pinke.config;

import com.fafu.kongshu.zhengxianyou.pinke.sqlitedb.NoteMeteData;

/**
 * Created by zhengxianyou on 2016/10/23.
 * 常量数据
 */

public class Constant {
    public static final String SP_NAME = "PinKe";

    public static final String Bmob_APPID = "45191dcaa84d7e078e5bd749b1e809d7";

    public static final String GAODE_KEY =  "763830f5ccae4bd395ad9f74f4c761b8";

    public static final String DB_NAME = "note.db";

    public final static int VERSION = 1;

    public static final String CREATE_TABLE_MY_NOTE =
            "CREATE TABLE "+ NoteMeteData.MyNoteTable.TABLE_NAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "title TEXT,origin TEXT,destination TEXT,time TEXT,datetime TEXT,content TEXT,phoneNumber TEXT,currentLocation TEXT,myIcon TEXT,nickName TEXT,objectId,TEXT)";

    public static final String DROP_TABLE_MY_NOTE = "DROP TABLE IF EXISTS "+NoteMeteData.MyNoteTable.TABLE_NAME;

     public static final String CREATE_TABLE_DISPLAY_NOTE =
            "CREATE TABLE "+ NoteMeteData.DisplayNoteTable.TABLE_NAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "title TEXT,origin TEXT,destination TEXT,time TEXT,datetime TEXT,content TEXT,phoneNumber TEXT,currentLocation TEXT,myIcon TEXT,nickName TEXT)";

    public static final String DROP_TABLE_DISPLAY_NOTE = "DROP TABLE IF EXISTS "+NoteMeteData.DisplayNoteTable.TABLE_NAME;


}
