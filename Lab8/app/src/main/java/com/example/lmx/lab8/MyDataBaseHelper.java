package com.example.lmx.lab8;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lmx on 2017/12/18.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper{
    private static final String SQL_CREATE_TABLE="CREATE TABLE Contacts("
            +"id integer primary key autoincrement,"
            +"name text unique not null, "
            +"birthday text, "
            +"gift text);";

    /*创建数据库访问对象 它实际上没有创建数据库，马上返回。   只有调用 getWritableDatabase() 或
    * getReadableDatabase()  时才会创建数据库  数据库文件位于 /data/data/< 包名 >/databases
    */
    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //  第一次调用 getWritableDatabase()  或 getReadableDatabase()  时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);//创建数据库表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

