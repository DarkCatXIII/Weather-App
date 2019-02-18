package com.play.ata.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper{

    public MyHelper(Context context) {
        super(context, "WEATHER", null, 01);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE WEATHER(ID INTEGER PRIMARY KEY AUTOINCREMENT,CODE VARCHAR(3),NOW VARCHAR(3),HIGH VARCHAR(3),LOW VARCHAR(3),TEXT VARCHAR(20),CITY VARCHAR(20),DATE VARCHAR(20),SPEED VARCHAR(6),SUNSET VARCHAR(6),SUNRISE VARCHAR(6))");
        db.execSQL("CREATE TABLE WEATHERALL(ID INTEGER PRIMARY KEY AUTOINCREMENT,HIGH VARCHAR(3),LOW VARCHAR(3),TEXT VARCHAR(20),DATE VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE WEATHER");
        db.execSQL("DROP TABLE WEATHERALL");
        onCreate(db);
    }

}
