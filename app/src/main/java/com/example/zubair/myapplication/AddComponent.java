package com.example.zubair.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class AddComponent extends AppCompatActivity {
private Spinner Category_Sp;
    DataBaseController dbh;
    EditText item_name,quantity,Location,Description;
    Spinner category;
    private String[] categories_Names;
    Button save,cancel;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    Asset mAsset;
    private static final  int REQUEST_PHOTO = 2;
    private static final int PERMISSION_REQUEST_CONTACT=3;
    private  static final String ID = "com.example.zubair.myapplication.ID";
    private  static final String cat = "com.example.zubair.myapplication.cat";

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_component);
        dbh=DataBaseController.get(this);

        Cursor cursor = dbh.getAllCategoriesForSpinner();
        if(cursor!=null){
            categories_Names = new String[cursor.getCount()];
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                this.categories_Names[i] = cursor.getString(0);
                i++;
                cursor.moveToNext();
            }
            cursor.close();
        }
        else
        {
            categories_Names = new String[2];
            this.categories_Names[0] = "<.....>";
            this.categories_Names[1] = "<No Category exists>";
        }
        Category_Sp = (Spinner) findViewById(R.id.Category_Spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories_Names);
        Category_Sp.setAdapter(adapter);
        item_name= (EditText) findViewById(R.id.item_name);
        quantity= (EditText) findViewById(R.id.quantity);
        Location= (EditText) findViewById(R.id.Location);
        Description= (EditText) findViewById(R.id.Description);
        category= (Spinner) findViewById(R.id.Category_Spinner);
        save= (Button) findViewById(R.id.Save_btn);

        String categoryI = (String) getIntent().getSerializableExtra(cat);
        String id;
        id = (String) getIntent().getSerializableExtra(ID) ;
        if (id ==null) {
            mAsset = new Asset();
        }
        else
        {
            mAsset = dbh.getShowComponent(categoryI,id);
            item_name.setText(mAsset.getItem_Name());
            quantity.setText(mAsset.getQuantity());
            Location.setText(mAsset.getLocation());
            Description.setText(mAsset.getDescription());
            category.setEnabled(false);
            save.setText("Update");

        }

        mPhotoFile = getPhotoFile(mAsset.getPhoto_file_path());



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsset.setItem_Name(item_name.getText().toString());
                mAsset.setQuantity(quantity.getText().toString());
                mAsset.setLocation(Location.getText().toString());
                mAsset.setDescription(Description.getText().toString());
                mAsset.setPhoto_file_path(mAsset.getPhoto_file_path());
                mAsset.setCategory(category.getSelectedItem().toString());
                if (save.getText().toString().equalsIgnoreCase("Update")) {

                    Boolean check=dbh.updateComponent(mAsset);
                    if (check)
                        Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_LONG).show();
                }
                Cursor cursor=dbh.AddComponent(mAsset);
                if(cursor==null) {
                    Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_LONG).show();
                    item_name.setText("");
                    quantity.setText("");
                    Location.setText("");
                    Description.setText("");
                    mPhotoView.setImageBitmap(null);

                }
                else
                {
                    //
                    save.setText("Update");
                    cursor.moveToFirst();
                    Asset mAsset = new Asset();
                    mAsset.setId(cursor.getString(0));
                    mAsset.setItem_Name(cursor.getString(2));
                    mAsset.setLocation(cursor.getString(3));
                    mAsset.setQuantity(cursor.getString(4));
                    mAsset.setDescription(cursor.getString(5));
                    mAsset.setPhoto_file_path(cursor.getString(6));
                    mAsset.setCategory(Category_Sp.getSelectedItem().toString());
                    cursor.close();
                    Boolean check=dbh.updateComponent(mAsset);
                    if (check)
                        Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_LONG).show();
                }

            }
        });
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoButton = (ImageButton) findViewById(R.id.crime_camera);
        mPhotoView = (ImageView)findViewById(R.id.imageView2);
        updatePhotoView();
        boolean canTakePhoto = mPhotoFile!=null && captureImage.resolveActivity(getPackageManager()) !=null;
        //check karega k koi app hai ya nahi camera ki.
        mPhotoButton.setEnabled(canTakePhoto);
        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

    }
    public void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
          //  Toast.makeText(getApplicationContext(),"No image Found",Toast.LENGTH_SHORT).show();
            mPhotoView.setImageDrawable(null);
        } else {
            Toast.makeText(getApplicationContext(),"image Found but not showing",Toast.LENGTH_SHORT).show();
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),this);
           //   Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());

            mPhotoView.setImageBitmap(bitmap);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode== REQUEST_PHOTO){
            updatePhotoView();
        }

    }
    public File getPhotoFile(String path){
        File externalFilesDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null)  //just to check k external storage hai k nahi..agar nahi hai tw null return karega.
            return  null;

        return new File(externalFilesDir,path);
    }
    public static Intent newIntent(Context packContext,String Id,String category){
        Intent intent = new Intent(packContext,AddComponent.class);
        intent.putExtra(ID,Id);
        intent.putExtra(cat,category);
        return intent;
    }
}
