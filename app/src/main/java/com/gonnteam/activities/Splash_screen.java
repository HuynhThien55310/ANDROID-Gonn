package com.gonnteam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gonnteam.R;

public class Splash_screen extends AppCompatActivity {
    public TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        tv = (TextView) findViewById(R.id.textView2);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);
        tv.startAnimation(anim);
        final Intent i = new Intent(this, MainActivity.class);
        Thread timer  = new Thread(){
            public void run (){
                try{
                    sleep(1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
