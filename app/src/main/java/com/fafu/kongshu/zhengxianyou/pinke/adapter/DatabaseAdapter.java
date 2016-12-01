package com.fafu.kongshu.zhengxianyou.pinke.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fafu.kongshu.zhengxianyou.pinke.bean.Note;
import com.fafu.kongshu.zhengxianyou.pinke.config.Config;
import com.fafu.kongshu.zhengxianyou.pinke.sqlitedb.DatabaseHelper;
import com.fafu.kongshu.zhengxianyou.pinke.sqlitedb.NoteMeteData;

import java.util.ArrayList;

/**
 * Created by zhengxianyou on 2016/11/28.
 */

public class DatabaseAdapter {
    private DatabaseHelper dbHelper;

    public DatabaseAdapter(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    //增
    public void rawAdd(Note note, String table) {
        if (table.equals(NoteMeteData.MyNoteTable.TABLE_NAME)) {
            String sql = "insert or ignore into " + NoteMeteData.MyNoteTable.TABLE_NAME +
                    "(title,origin,destination,time,datetime,content,phoneNumber,currentLocation,myIcon,nickName,objectId) values (?,?,?,?,?,?,?,?,?,?,?)";
            Object[] args = {note.getTitle(), note.getOrigin(), note.getDestination(), note.getTime(), note.getDatetime(),
                    note.getContent(), note.getPhoneNumber(), note.getCurrentLocation(), note.getMyIcon(), note.getNickName(),note.getObjectId()};

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(sql, args);
            db.close();
        }else {
        String sql = "insert into " + NoteMeteData.DisplayNoteTable.TABLE_NAME +
                "(title,origin,destination,time,datetime,content,phoneNumber,currentLocation,myIcon,nickName) values (?,?,?,?,?,?,?,?,?,?)";
        Object[] args = {note.getTitle(), note.getOrigin(), note.getDestination(), note.getTime(), note.getDatetime(),
                note.getContent(), note.getPhoneNumber(), note.getCurrentLocation(), note.getMyIcon(), note.getNickName()};

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql, args);
        db.close();
        }
    }

    //删(清空表中数据)
    public void rawDelete(String table) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "delete from " + table;
        db.execSQL(sql);
        db.close();
    }


    //删一条数据
    public void delete(String objectId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = NoteMeteData.MyNoteTable.OBJECTID + "=?";
        String[] whereArgs = {objectId};

        //表名，依据条件，条件的值
        db.delete(NoteMeteData.MyNoteTable.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }


    //改
    public void rawUpdate(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "update "+NoteMeteData.MyNoteTable.TABLE_NAME+
                " set origin=?,destination=?,time=?,content=?,phoneNumber=?,currentLocation=? where objectId=?";
        Object[] args =
                {note.getOrigin(),note.getDestination(),note.getTime(),note.getDatetime(),note.getContent(),note.getPhoneNumber(),note.getCurrentLocation(),note.getNoteId()};
        db.execSQL(sql, args);
        db.close();
    }

    public void update(Note note,String objectId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteMeteData.MyNoteTable.ORIGIN, note.getOrigin());
        values.put(NoteMeteData.MyNoteTable.DESTINATION, note.getDestination());
        values.put(NoteMeteData.MyNoteTable.TIME, note.getTime());
        values.put(NoteMeteData.MyNoteTable.CONTENT, note.getContent());
        values.put(NoteMeteData.MyNoteTable.PHONENUMBER, note.getPhoneNumber());
        values.put(NoteMeteData.MyNoteTable.CURRENTLOCATION, note.getCurrentLocation());
        String whereClause = NoteMeteData.MyNoteTable.OBJECTID + "=?";
        String[] whereArgs = {objectId};
        db.update(NoteMeteData.MyNoteTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    //查
    public ArrayList<Note> rawQueryAll(String table) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String sql = "select title,origin,destination,time,datetime,content,phoneNumber,currentLocation,myIcon,nickName,objectId from " + table;
        String sql2 = "select title,origin,destination,time,datetime,content,phoneNumber,currentLocation,myIcon,nickName from " + table;

        ArrayList<Note> notes = new ArrayList<>();

//        if (table.equals(NoteMeteData.MyNoteTable.TABLE_NAME)) {
//            Cursor c = db.rawQuery(sql, null);
//            while (c.moveToNext()) {
//                Note note = new Note();
//                note.setNoteId(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.OBJECTID)));
//                note.setTitle(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.TITLE)));
//                note.setOrigin(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.ORIGIN)));
//                note.setDestination(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.DESTINATION)));
//                note.setTime(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.TIME)));
//                note.setDatetime(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.DATETIME)));
//                note.setPhoneNumber(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.PHONENUMBER)));
//                note.setContent(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.CONTENT)));
//                note.setCurrentLocation(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.CURRENTLOCATION)));
//                note.setMyIcon(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.MYICON)));
//                note.setNickName(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.NICKNAME)));
//                notes.add(note);
//            }
//            c.close();
//
//        } else {
            Cursor c = db.rawQuery(sql2, null);
            while (c.moveToNext()) {
                Note note = new Note();
                note.setTitle(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.TITLE)));
                note.setOrigin(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.ORIGIN)));
                note.setDestination(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.DESTINATION)));
                note.setTime(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.TIME)));
                note.setDatetime(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.DATETIME)));
                note.setPhoneNumber(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.PHONENUMBER)));
                note.setContent(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.CONTENT)));
                note.setCurrentLocation(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.CURRENTLOCATION)));
                note.setMyIcon(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.MYICON)));
                note.setNickName(c.getString(c.getColumnIndexOrThrow(NoteMeteData.DisplayNoteTable.NICKNAME)));
                notes.add(note);

            }
            c.close();

//        }

            db.close();
            return notes;

    }


    //查所有
    public ArrayList<Note> queryAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {NoteMeteData.MyNoteTable.TITLE,NoteMeteData.MyNoteTable.ORIGIN,NoteMeteData.MyNoteTable.DESTINATION,
                NoteMeteData.MyNoteTable.TIME,NoteMeteData.MyNoteTable.DATETIME,NoteMeteData.MyNoteTable.CONTENT,NoteMeteData.MyNoteTable.PHONENUMBER,
                NoteMeteData.MyNoteTable.CURRENTLOCATION,NoteMeteData.MyNoteTable.MYICON,NoteMeteData.MyNoteTable.NICKNAME,NoteMeteData.MyNoteTable.OBJECTID};
        //是否去除重复记录，表名，要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，分页条件
        Cursor c = db.query(true, NoteMeteData.MyNoteTable.TABLE_NAME, columns, NoteMeteData.MyNoteTable.NICKNAME+ "=?", new String[]{Config.getNickName()}, null, null, null, null);

        ArrayList<Note> notes = new ArrayList<>();
        Note note = null;

        while (c.moveToNext()) {
            note = new Note();
            note.setNoteId(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.OBJECTID)));
            note.setTitle(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.TITLE)));
            note.setOrigin(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.ORIGIN)));
            note.setDestination(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.DESTINATION)));
            note.setTime(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.TIME)));
            note.setDatetime(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.DATETIME)));
            note.setPhoneNumber(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.PHONENUMBER)));
            note.setContent(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.CONTENT)));
            note.setCurrentLocation(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.CURRENTLOCATION)));
            note.setMyIcon(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.MYICON)));
            note.setNickName(c.getString(c.getColumnIndexOrThrow(NoteMeteData.MyNoteTable.NICKNAME)));
            notes.add(note);
        }
        c.close();
        db.close();
        return notes;
    }
}