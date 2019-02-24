package com.example.zubair.myapplication;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Zubair on 03-Apr-17.
 */
public class Asset {

    public Asset(String ID,String Date,String Item_Name,String Location,String Quantity,String Description,String photo_file_path,String Category){
        this.id=ID;
        this.mDate=Date;
        this.Item_Name=Item_Name;
        this.Location=Location;
        this.Quantity=Quantity;
        this.Description=Description;
        this.photo_file_path=photo_file_path;
        this.Category=Category;
    }
    public Asset(){
        this.id = UUID.randomUUID().toString();
    }

    private String id;
    private String mDate;
    private String Item_Name;
    private String Location;
    private String Quantity;
    private String Description;
    private String photo_file_path;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    private String Category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id =  id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date.toString();
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhoto_file_path() {
        return "IMG_"+getId().toString()+".jpg";
    }
    public void setPhoto_file_path(String photo_file_path) {
        this.photo_file_path = photo_file_path;
    }



}
