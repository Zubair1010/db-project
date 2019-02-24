package com.example.zubair.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 * Created by Zubair on 23-Mar-17.
 */
public class DataBaseController {
    private static DataBaseController DbController;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    public boolean check = false;
    int [] arr;
    public static DataBaseController get(Context context) {
        if (DbController == null) {
            DbController = new DataBaseController(context);
        }
        return DbController;
    }

    private DataBaseController(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DataBaseHelper(mContext).getWritableDatabase();
        check = DataBaseHelper.IsUser_InfoTableExists(mDatabase);


    }

    public void createUserTable() {
        if(!DataBaseHelper.IsUser_InfoTableExists(mDatabase))
        DataBaseHelper.createUserTable(mDatabase);
    }

    public boolean addUser(String username, String password, String Email, String Company, String com_logo,String User_Role) {
        try {
            mDatabase.execSQL("Insert INTO User_Info(User_Name,Password,Email,Company_Name,company_logo_path,User_Role) VALUES('" + username + "','" + password + "','" + Email + "','"
                    + Company + "','" + com_logo + "','"+User_Role+"')");
        }
        catch (Exception e){
            return  false;
        }
        return true;
    }

    public boolean CheckAccount(String userName, String Password) {
        String query = "SELECT * FROM User_Info where User_name LIKE '%" + userName + "%' AND Password LIKE '%" + Password + "%'";
        Cursor cursor = mDatabase.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean AddCategory(String C_name) {
        Cursor cursor = mDatabase.rawQuery("select * FROM Categories where Category_name LIKE '%" + C_name + "%'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        mDatabase.execSQL("Insert INTO Categories(Category_name) Values('" + C_name + "')");
        mDatabase.execSQL("create table " + C_name + "(ID,Date,Item_Name,Location,Quantity,Description,Photo_file_Path)");
        return false;
    }

    public Cursor AddComponent(Asset mAsset) {
        Cursor cursor = mDatabase.rawQuery("select * FROM " + mAsset.getCategory() + " where Item_name LIKE '%" + mAsset.getItem_Name() + "%' AND Location Like '%"+mAsset.getLocation()+"%'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return cursor;
            }
            cursor.close();
        }
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mDatabase.execSQL("Insert INTO " + mAsset.getCategory() + "(ID,Date,Item_Name,Location,Quantity,Description,Photo_file_Path)" +
                " values ('" + mAsset.getId() + "' ,'" + date + "' ,'" + mAsset.getItem_Name() + "' ,'" + mAsset.getLocation() + "','" + mAsset.getQuantity() + "','" + mAsset.getDescription() + "','" + mAsset.getPhoto_file_path() + "')");
        return null;

    }

    public Cursor getAllCategoriesForSpinner() {
        Cursor cursor = mDatabase.rawQuery("Select * from Categories", null);
        return cursor;
    }

   public Cursor getUserInfo(String user){
       Cursor cursor = mDatabase.rawQuery("Select * from User_Info where User_name LIKE '%" + user + "%'",null);
       if(cursor!=null){
           cursor.moveToFirst();
           return cursor;
       }
       return null;
   }

    public boolean updateComponent(Asset mAsset) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mDatabase.execSQL("Update " + mAsset.getCategory() + " SET Date = '"+date+"', Item_Name = '" + mAsset.getItem_Name() + "' , Location='" + mAsset.getLocation() + "' , Quantity='" + mAsset.getQuantity() + "' , Description='" + mAsset.getDescription() + "' , photo_file_path='" + mAsset.getPhoto_file_path() + "' Where ID LIKE '%" + mAsset.getId() +"%'");
        return true;
    }

    public List<Asset> getAssetsForCategory(String category){
        List<Asset> list=new ArrayList<Asset>();
        int count=0;
        Cursor cursor=mDatabase.rawQuery("Select * from "+category,null);
        if(cursor.getCount()==0){
            return null;
        }
        if(cursor!=null)
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                list.add(new Asset(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),category));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;

    }
    public Asset getShowComponent(String category,String id){
        Cursor cursor = mDatabase.rawQuery("Select * from " + category + " where ID LIKE '%" + id + "%'",null);
        if(cursor!=null) {
            cursor.moveToFirst();
            Asset aa= new Asset(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), category);
            cursor.close();
            return aa;
        }
        return null;

    }
    public List<Asset> getAssetsForMain() {
        List<Asset> list = new ArrayList<Asset>();
        int count=0;
        Cursor cursor = mDatabase.rawQuery("Select * from Categories", null);

            if (cursor.getCount() == 0) {
                return null;
            }
        String [] arr = new String[cursor.getCount()];
            if(cursor!=null){
                cursor.moveToFirst();
                while (count<cursor.getCount()){
                    arr[count]=cursor.getString(0);
                    cursor.moveToNext();
                    count++;
                }
                cursor.close();;

            }
        Cursor [] cursors = new Cursor[count];

        for (int i = 0; i < cursor.getCount(); i++) {
            cursors[i] = mDatabase.rawQuery("Select * from " + arr[i], null);
            if(cursors[i]!=null){
                cursors[i].moveToFirst();
                while (!cursors[i].isAfterLast()){
                    list.add(new Asset(cursors[i].getString(0),cursors[i].getString(1),cursors[i].getString(2),cursors[i].getString(3),cursors[i].getString(4),cursors[i].getString(5),cursors[i].getString(6),arr[i]));
                cursors[i].moveToNext();

                }
                cursors[i].close();
            }

        }

return list;
    }
    public boolean checkCursors(){
        for(int i=0;i<arr.length;i++){
            if(arr[i]==0)
                return true;

        }
        return false;
    }
    public List<String> getCategories(){
        List<String> cat = new ArrayList<String>();

        Cursor cursor = mDatabase.rawQuery("Select * from Categories", null);
        if(cursor!=null){
            cursor.moveToFirst();
            while ((!cursor.isAfterLast())){
                cat.add(cursor.getString(0));
                cursor.moveToNext();

            }
            cursor.close();
        }
        return  cat;
    }

    public File getPhotoFile(Asset asset){
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null)  //just to check k external storage hai k nahi..agar nahi hai tw null return karega.
            return  null;

        return new File(externalFilesDir,asset.getPhoto_file_path());
    }
    public List<User> UsersInfo(){
        List<User> mUserList = new ArrayList<User>();
        Cursor cursor = mDatabase.rawQuery("Select * from User_Info",null);
        if(cursor!=null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                mUserList.add(new User(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return mUserList;
    }
    public boolean UpdateUser(String User,String Password,String Email){
         try {
             mDatabase.execSQL("Update User_Info SET Password = '" + Password + "', Email = '" + Email + "' where User_name LIKE '%" + User + "%'");
             return true;
         }
       catch (Exception e){
            return false;
        }
    }
    public boolean DeleteComponent(String ID,String category){
        try {
            mDatabase.execSQL("DELETE FROM "+category+" Where ID LIKE '%" + ID + "%'");
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public boolean DeleteUser(String User){
        try {
            Cursor cursor=mDatabase.rawQuery("SELECT * FROM User_Info",null);
            if(cursor.getCount()==1) {
              return false;
            }
            mDatabase.execSQL("Delete from User_Info where User_name LIKE '%" + User + "%'");
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
