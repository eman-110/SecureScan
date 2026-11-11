package com.example.securescan;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAnimationAndTransition();
    }

    private void startAnimationAndTransition() {
        Animation stompAnimation = AnimationUtils.loadAnimation(this, R.anim.stomp);
        View logo = findViewById(R.id.logoImage);
        View dot1 = findViewById(R.id.dot1);
        View dot2 = findViewById(R.id.dot2);
        View dot3 = findViewById(R.id.dot3);
        logo.startAnimation(stompAnimation);

        animateDot(dot1, 0);
        animateDot(dot2, 200);
        animateDot(dot3, 400);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Activity_Splash.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // 3 seconds delay
    }

    private void animateDot(View dot, int delay) {
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(dot, "alpha", 0.3f, 1f);
        fadeAnimator.setDuration(500);
        fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
        fadeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        fadeAnimator.setStartDelay(delay);
        fadeAnimator.start();
    }
}
