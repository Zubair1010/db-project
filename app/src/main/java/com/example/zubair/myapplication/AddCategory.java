package com.example.zubair.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategory extends AppCompatActivity {
    Button Add,Cancel;
    DataBaseController dbh;
    EditText Category_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        dbh = DataBaseController.get(getApplicationContext());

        Add = (Button) findViewById(R.id.Add_category_btn);
        Category_editText = (EditText) findViewById(R.id.category_edit_text) ;
        Add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean result = dbh.AddCategory(Category_editText.getText().toString());
                if(result){
                    Toast.makeText(getApplicationContext(),"Category Already Exists",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Successfully Added..!!",Toast.LENGTH_LONG).show();
                    Category_editText.setText("");
                }
            }

        });
        Cancel = (Button) findViewById(R.id.cancel_Add_category_btn);
        Cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
