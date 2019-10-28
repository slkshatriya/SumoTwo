package com.one4all.sumotwo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(SplashScreen.this,LatestMessageActivity.class);
            startActivity(intent);
            finish();
        }
//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent=new Intent(SplashScreen.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//        },3000);
//
    }
}
