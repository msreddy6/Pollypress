package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageText = findViewById(R.id.main_msg_txt);
        messageText.setText("Hello Mahdi");

        messageText.setTranslationY(-1000); // Starting from above the screen
        ObjectAnimator animator = ObjectAnimator.ofFloat(messageText, "translationY", 0);
        animator.setDuration(3000); // Animation duration in ms
        animator.setInterpolator(new AccelerateInterpolator()); // Smooth effect
        animator.start();
    }
}
