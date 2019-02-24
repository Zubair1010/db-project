package com.example.zubair.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zubair on 23-Mar-17.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static  final int VERSION = 1;
    private static final String DATABASE_NAME = "AssetManager.db";
    public DataBaseHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Categories(Category_name)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public  static  void createTable(String str, SQLiteDatabase db){


    }
    public  static  void createUserTable(SQLiteDatabase db){
        db.execSQL("create table User_Info(User_Name primary key,Password,Email,Company_Name,company_logo_path,User_Role)");
    }
    public static boolean IsUser_InfoTableExists(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'User_Info'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;

    }
}
