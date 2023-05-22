package com.example.administrator.lztsg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MultipleItem;

import java.util.ArrayList;

public class Dao {
    public ArrayList<MultipleItem> detalist = new ArrayList<>();
    public ArrayList<MultipleItem> detaMasslist = new ArrayList<>();
    public ArrayList<MultipleItem> detaTaglist = new ArrayList<>();
    private final DatabaseHelper mHelper;

    public Dao(Context context) {
        //创建数据库
        mHelper = new DatabaseHelper(context);
    }

    public void insert(String str) {
        //增加
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "insert into " + Constants.TABLE_NAME_MIAN + "(_id,name,Introduction,preferences,imgurl) values(?,?,?,?,?)";
        db.execSQL(sql, new Object[]{"", "", "", 0, str});
        Log.d("id:", "" + str);
        db.close();
    }

    public void delete() {
        //刪除
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "delete from " + Constants.TABLE_NAME_MIAN + " where preferences = 0";
        db.execSQL(sql);
        db.close();
    }

    public void update(int live) {
        //更改
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "update " + Constants.TABLE_NAME_MIAN + " set preferences = " + live;
        db.execSQL(sql);
        db.close();
    }

    public void update(int live, String wherestr) {
        //更改
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "update " + Constants.TABLE_NAME_MIAN + " set preferences = '" + live + "'" + " where name = '" + wherestr + "'";
        db.execSQL(sql);
        db.close();
    }

    public void query(String table, String obj) {
        int index, index1, index2, index3, index4, index5, index6 = 0;
        int preferences, mass;
        String id, name, Introduction, imgurl, tag = null;
        //查詢
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from " + table + " order by " + obj;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            switch (table) {
                case Constants.TABLE_NAME_MIAN:
                    index = cursor.getColumnIndex("_id");
                    index1 = cursor.getColumnIndex("name");
                    index2 = cursor.getColumnIndex("Introduction");
                    index3 = cursor.getColumnIndex("preferences");
                    index4 = cursor.getColumnIndex("imgurl");
                    index5 = cursor.getColumnIndex("itemmassid");
                    index6 = cursor.getColumnIndex("itemtag");

                    id = cursor.getString(index);
                    name = cursor.getString(index1);
                    Introduction = cursor.getString(index2);
                    preferences = cursor.getInt(index3);
                    imgurl = cursor.getString(index4);
                    mass = cursor.getInt(index5);
                    tag = cursor.getString(index6);
                    detalist.add(new Item(id, name, Introduction, preferences, imgurl, mass, tag));
                    break;
                case Constants.TABLE_NAME_MASS:
                    index = cursor.getColumnIndex("mass_id");
                    index1 = cursor.getColumnIndex("massname");
                    id = cursor.getString(index);
                    name = cursor.getString(index1);

                    detaMasslist.add(new Item(id, name));
                    break;
                case Constants.TABLE_NAME_TAG:
                    index = cursor.getColumnIndex("tag_id");
                    index1 = cursor.getColumnIndex("tagname");

                    id = cursor.getString(index);
                    name = cursor.getString(index1);

                    detaTaglist.add(new Item(id, name));
                    break;
            }
        }
        cursor.close();
        db.close();
    }

    public void query(String table, String wherestr, String obj) {
        int index, index1, index2, index3, index4, index5, index6 = 0;
        int preferences, mass;
        String id, name, Introduction, imgurl, tag = null;
        //查詢+条件
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from " + table + " where " + wherestr + " = '" + obj + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            switch (table) {
                case Constants.TABLE_NAME_MIAN:
                    index = cursor.getColumnIndex("_id");
                    index1 = cursor.getColumnIndex("name");
                    index2 = cursor.getColumnIndex("Introduction");
                    index3 = cursor.getColumnIndex("preferences");
                    index4 = cursor.getColumnIndex("imgurl");
                    index5 = cursor.getColumnIndex("itemmassid");
                    index6 = cursor.getColumnIndex("itemtag");

                    id = cursor.getString(index);
                    name = cursor.getString(index1);
                    Introduction = cursor.getString(index2);
                    preferences = cursor.getInt(index3);
                    imgurl = cursor.getString(index4);
                    mass = cursor.getInt(index5);
                    tag = cursor.getString(index6);
                    detalist.add(new Item(id, name, Introduction, preferences, imgurl, mass, tag));
                    break;
                case Constants.TABLE_NAME_MASS:
                    index = cursor.getColumnIndex("mass_id");
                    index1 = cursor.getColumnIndex("massname");
                    id = cursor.getString(index);
                    name = cursor.getString(index1);

                    detaMasslist.add(new Item(id, name));
                    break;
                case Constants.TABLE_NAME_TAG:
                    index = cursor.getColumnIndex("tag_id");
                    index1 = cursor.getColumnIndex("tagname");

                    id = cursor.getString(index);
                    name = cursor.getString(index1);

                    detaTaglist.add(new Item(id, name));
                    break;
            }

        }
        cursor.close();
        db.close();
    }
}
