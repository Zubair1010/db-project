package com.example.zubair.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserDetails extends AppCompatActivity {

    private static final String Username="com.example.zubair.myapplication.username";
    private EditText userName,Userpassword,UserConfirmPassword,UserEmail;
    private Button Update,Cancel;
    DataBaseController dbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        dbh = DataBaseController.get(getApplicationContext());
        userName = (EditText) findViewById(R.id.user_detail_username);
        Userpassword = (EditText) findViewById(R.id.user_detail_password);
        UserConfirmPassword = (EditText) findViewById(R.id.user_detail_con_Password);
        UserEmail = (EditText) findViewById(R.id.user_detail_email);
        Update = (Button) findViewById(R.id.btn_update_user);
        Cancel=(Button)findViewById(R.id.btn_cancel_user_detail);

        final String User = (String) getIntent().getSerializableExtra(Username);
        if(User!=null) {
           Cursor cursor= dbh.getUserInfo(User);
            cursor.moveToFirst();
            userName.setText(cursor.getString(0));
            userName.setEnabled(false);
            Userpassword.setText(cursor.getString(1));
            UserConfirmPassword.setText(cursor.getString(1));
            UserEmail.setText(cursor.getString(2));

            cursor.close();

        }

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Userpassword.getText().toString().equalsIgnoreCase("")||userName.getText().toString().equalsIgnoreCase("")||UserConfirmPassword.getText().toString().equalsIgnoreCase("")||userName.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(getApplicationContext(),"Fill the Required Fields",Toast.LENGTH_LONG).show();
                else
                if (UserConfirmPassword.getText().toString().equalsIgnoreCase(Userpassword.getText().toString())){
                    //Toast.makeText(getApplicationContext(),userName.getText().toString()+Userpassword.getText().toString()+UserEmail.getText().toString(),Toast.LENGTH_LONG).show();
                   boolean check = dbh.UpdateUser(userName.getText().toString(),Userpassword.getText().toString(),UserEmail.getText().toString());
                    if(check==true) {
                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                        finish();

                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error Occured While Updating", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Username doesnot exist",Toast.LENGTH_LONG).show();


            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public static Intent newIntent(Context packageContext, String username){
        Intent intent = new Intent(packageContext,UserDetails.class);
        intent.putExtra(Username,username);
        return intent;
    }
}
