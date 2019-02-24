package com.example.zubair.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splash_screen extends AppCompatActivity {
    ImageView splashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashImage= (ImageView) findViewById(R.id.imageView);
        Animation ani=new AnimationUtils().loadAnimation(getBaseContext(),R.anim.rotate);
        splashImage.startAnimation(ani);

        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                DataBaseController dbh = DataBaseController.get(getApplicationContext());
                if (dbh.check == true) {
                    Intent intent = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), Signup.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
