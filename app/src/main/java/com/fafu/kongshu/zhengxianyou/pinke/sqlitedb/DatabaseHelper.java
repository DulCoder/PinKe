package com.fafu.kongshu.zhengxianyou.pinke.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fafu.kongshu.zhengxianyou.pinke.config.Constant;

import static com.fafu.kongshu.zhengxianyou.pinke.PinKeApplication.editor;


/**
 * Created by zhengxianyou on 2016/11/28.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, Constant.DB_NAME, null, Constant.VERSION);
    }

    /**
     * 如果数据库不存在会调用该方法
     * @param db 用于操作数据库的工具类
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constant.CREATE_TABLE_MY_NOTE);
        db.execSQL(Constant.CREATE_TABLE_DISPLAY_NOTE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(Constant.DROP_TABLE_MY_NOTE);
        db.execSQL(Constant.CREATE_TABLE_MY_NOTE);
        editor.putBoolean("isNeedRefresh", true);
        editor.commit();
//        db.execSQL(Constant.DROP_TABLE_DISPLAY_NOTE);
//        db.execSQL(Constant.CREATE_TABLE_DISPLAY_NOTE);

    }
}
