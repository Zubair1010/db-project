package com.example.zubair.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Welcome extends AppCompatActivity {
    Animation fade_in,fade_out;
    ViewFlipper viewFlipper;
    Button login;
    EditText userName,password;
    DataBaseController dbh;
    public static final String user= "User";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        dbh = DataBaseController.get(getApplicationContext());

        viewFlipper=(ViewFlipper)findViewById(R.id.ViewFlipper);
        fade_in= AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
        fade_out= AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        //set auto flipper
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();
        userName = (EditText) findViewById(R.id.Login_Username);
        password = (EditText) findViewById(R.id.Login_Password);
        login= (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equalsIgnoreCase("")&& password.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter All Required Feilds", Toast.LENGTH_LONG).show();
                }
                else{
                    if(dbh.CheckAccount(userName.getText().toString(),password.getText().toString())){
                        finish();
                        Intent intent = MainActivity.newIntent(getApplicationContext(),userName.getText().toString());
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid User Name OR Password..!!\nTry Again..!!", Toast.LENGTH_LONG).show();
                    }



                }


            }
        });

    }


}
