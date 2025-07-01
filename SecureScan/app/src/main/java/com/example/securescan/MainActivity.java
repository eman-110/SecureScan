package com.example.securescan;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.Manifest;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 100; // any unique co
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCameraPermission();

    }


    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        } else {
            startAnimationAndTransition();
        }
    }
    private void startAnimationAndTransition() {
        // Load stomp animation from XML
        Animation stompAnimation = AnimationUtils.loadAnimation(this, R.anim.stomp);
        View logo = findViewById(R.id.logoImage);
        View dot1 = findViewById(R.id.dot1);
        View dot2 = findViewById(R.id.dot2);
        View dot3 = findViewById(R.id.dot3);
        logo.startAnimation(stompAnimation);

        // Start 3-dot animation
        animateDot(dot1, 0);
        animateDot(dot2, 200);
        animateDot(dot3, 400);

        // Delay transition to Activity2
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Activity_Splash.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // 3 seconds delay
    }

    private void animateDot(View dot, int delay) {
        dot.setAlpha(0f);
        dot.animate().alpha(1f).setStartDelay(delay).setDuration(500).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startAnimationAndTransition(); // Start after permission is granted
            } else {
                Toast.makeText(this, "Camera permission is required to continue", Toast.LENGTH_SHORT).show();
                // Optionally close app or stay on screen
            }
        }
    }

    // Handle user response

    private void animateDot(View dot, long delay) {
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(dot, "alpha", 0.3f, 1f);
        fadeAnimator.setDuration(500);
        fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
        fadeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        fadeAnimator.setStartDelay(delay);
        fadeAnimator.start();
    }
}
