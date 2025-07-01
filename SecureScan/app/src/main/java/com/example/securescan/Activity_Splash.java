package com.example.securescan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Activity_Splash extends Activity {
    private int clickCount = 0; // Counter for button clicks
    private Button nextButton;
    private TextView textLine1, textLine2;
    private View dot1, dot2, dot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Ensure this matches your XML file

        nextButton = findViewById(R.id.buttonNavigate);
        TextView skipText = findViewById(R.id.skipText);
        textLine1 = findViewById(R.id.textLine1);
        textLine2 = findViewById(R.id.textLine2);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;

                if (clickCount == 1) {
                    applyTextChangeWithAnimation(textLine1, "Scan with confidence. Security at every step.", R.anim.slide_in_left);
                    applyTextChangeWithAnimation(textLine2, "", R.anim.slide_in_left);

                    // Change dots
                    dot1.setBackgroundResource(R.drawable.dot_background1);
                    dot2.setBackgroundResource(R.drawable.dot_background);
                    dot3.setBackgroundResource(R.drawable.dot_background1);
                } else if (clickCount == 2) {
                    applyTextChangeWithAnimation(textLine1, "Smart scans, secure journeys—", R.anim.slide_in_left);
                    applyTextChangeWithAnimation(textLine2, "protecting what matters", R.anim.slide_in_left);

                    // Change dots
                    dot1.setBackgroundResource(R.drawable.dot_background1);
                    dot2.setBackgroundResource(R.drawable.dot_background1);
                    dot3.setBackgroundResource(R.drawable.dot_background);

                    // Change button text
                    nextButton.setText("Get Started");
                } else if (clickCount == 3) {
                    // Navigate to Activity5
                    Intent intent = new Intent(Activity_Splash.this, Activity_Scan.class);
                    startActivity(intent);
                }
            }
        });

        // Navigate to Activity5 when "Skip" is clicked
        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Splash.this, Activity_Scan.class);
                startActivity(intent);
            }
        });
    }

    private void applyTextChangeWithAnimation(final TextView textView, final String newText, int animResource) {
        Animation slideIn = AnimationUtils.loadAnimation(this, animResource);
        textView.startAnimation(slideIn);
        textView.setText(newText);
    }
}
