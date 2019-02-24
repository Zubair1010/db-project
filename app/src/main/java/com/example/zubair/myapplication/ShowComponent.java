package com.example.zubair.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

public class ShowComponent extends AppCompatActivity {
    Button edit,delete;
    DataBaseController dbh;
    private String Id;
    private String Category;
    private Asset mAsset;
    TextView item_name,quantity,category,Location,Description;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private static final String id="com.example.zubair.myapplication.id";
    private static final String cat="com.example.zubair.myapplication.CAT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_component);
        dbh = DataBaseController.get(getApplicationContext());
        item_name= (TextView) findViewById(R.id.item_name_text);
        quantity= (TextView) findViewById(R.id.quantity_text);
        category= (TextView) findViewById(R.id.Category_Spinner);
        Location= (TextView) findViewById(R.id.Location_text);
        Description= (TextView) findViewById(R.id.Description_text);
        mPhotoView = (ImageView)  findViewById(R.id.imageView2);
        Category = (String) getIntent().getSerializableExtra(cat);
        Id = (String) getIntent().getSerializableExtra(id);
        mAsset=dbh.getShowComponent(Category,Id);
        if(mAsset!=null) {
            item_name.setText(mAsset.getItem_Name());
            quantity.setText(mAsset.getQuantity());
            category.setText(mAsset.getCategory());
            Location.setText(mAsset.getLocation());
            Description.setText(mAsset.getDescription());
            mPhotoFile = getPhotoFile(mAsset.getPhoto_file_path());
            updatePhotoView();



        }

        edit= (Button) findViewById(R.id.Edit_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=AddComponent.newIntent(getApplicationContext(),mAsset.getId(),mAsset.getCategory());
                startActivity(intent);
                      }
        });
        delete= (Button) findViewById(R.id.Delete_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=dbh.DeleteComponent(mAsset.getId().toString(),mAsset.getCategory().toString());
                if(check) {
                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Cant't Delete",Toast.LENGTH_LONG).show();
            }
        });
    }
    public static Intent newIntent(Context packageContext, String category,String Id){
        Intent intent = new Intent(packageContext,ShowComponent.class);
        intent.putExtra(cat,category);
        intent.putExtra(id,Id);
        return intent;
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
    public File getPhotoFile(String path){
        File externalFilesDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null)  //just to check k external storage hai k nahi..agar nahi hai tw null return karega.
            return  null;

        return new File(externalFilesDir,path);
    }
}
