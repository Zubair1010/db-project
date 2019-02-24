package com.example.zubair.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

public class Signup extends AppCompatActivity {
    EditText username,password,confirmPassword,email,company;
    private  static  final String ADD_USER_EXTRA = "com.example.myappliacation.UserExtra";
    Button Sign_up;
    ImageView mImageButton;
    String image_path = "";
    private File mPhotoFile;
    DataBaseController dbh;
    RadioGroup mRadioGroup;
    boolean checkNewUser = false;
    RadioButton admin_radio_btn;
    RadioButton user_radio_btn;
    public static final int REQUEST_PHOTO =1;
    String User_Role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        dbh = DataBaseController.get(getApplicationContext());

admin_radio_btn = (RadioButton) findViewById(R.id.Manager);
        user_radio_btn = (RadioButton) findViewById(R.id.user);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        username= (EditText) findViewById(R.id.edit_username);
        try {
            checkNewUser = (boolean) getIntent().getSerializableExtra(ADD_USER_EXTRA);
        }
        catch (Exception e){

        }
        password= (EditText) findViewById(R.id.edit_password);
        confirmPassword= (EditText) findViewById(R.id.edit_con_Password);
        email= (EditText) findViewById(R.id.edit_email);
        company= (EditText) findViewById(R.id.edit_company);
        mImageButton= (ImageView) findViewById(R.id.img_company_logo);
        Sign_up= (Button) findViewById(R.id.btn_signup);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoFile = getPhotoFile("IMG_CompanyLogo.jpg");
        boolean canTakePhoto = mPhotoFile!=null && captureImage.resolveActivity(getPackageManager()) !=null;
        //check karega k koi app hai ya nahi camera ki.
        mImageButton.setEnabled(canTakePhoto);
        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equalsIgnoreCase("") && password.getText().toString().equalsIgnoreCase("") && confirmPassword.getText().toString().equalsIgnoreCase("")
                        && email.getText().toString().equalsIgnoreCase("") && company.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter All Required Feilds", Toast.LENGTH_LONG).show();
                }
                    else{
                        if (password.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())) {

                            dbh.createUserTable();
                            if(admin_radio_btn.isChecked()){
                                User_Role = "Manager";
                            }
                            else
                            User_Role="User";
                           boolean check =  dbh.addUser(username.getText().toString(), password.getText().toString(), email.getText().toString(), company.getText().toString(),"IMG_CompanyLogo.jpg",User_Role);
                         if(check) {
                             finish();
                             if(!checkNewUser) {
                                 Intent intent = MainActivity.newIntent(getApplicationContext(), username.getText().toString());
                                 startActivity(intent);
                             }
                         }
                            else
                         {
                             Toast.makeText(getApplicationContext(), "User Name Already Exists..!! Choose a Different one..", Toast.LENGTH_LONG).show();
                         }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password doesnot Match", Toast.LENGTH_LONG).show();

                        }
                    }
                }

        });


    }
    public void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
             Toast.makeText(getApplicationContext(),"No image Found",Toast.LENGTH_SHORT).show();
            mImageButton.setImageDrawable(null);
        } else {
            Toast.makeText(getApplicationContext(),"image Found but not showing",Toast.LENGTH_SHORT).show();
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),this);
            //   Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());

            mImageButton.setImageBitmap(bitmap);
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
    public static Intent newIntent(Context packageContext, boolean check){
        Intent intent = new Intent(packageContext,Signup.class);
        intent.putExtra(ADD_USER_EXTRA,check);
        return intent;
    }

}
