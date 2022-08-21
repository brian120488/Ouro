package com.materialstudies.reply;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import com.materialstudies.reply.ui.MainActivity;


public class SplashPage extends AppCompatActivity {

    private static int Next = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, Next);
    }

}