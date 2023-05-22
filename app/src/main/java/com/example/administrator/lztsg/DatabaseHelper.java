package com.example.administrator.lztsg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * @ context  上下文
     * @ name     数据库名称
     * @ factory  游标工厂
     * @ version  版本号
     */
    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建库时回调
        Log.e("创建成果","创建成果");
        String sql = "create table " + Constants.TABLE_NAME_MIAN + "(_id varchar,name varchar,Introduction varchar,preferences char(1),imgurl varchar)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新库时回调
    }
}
