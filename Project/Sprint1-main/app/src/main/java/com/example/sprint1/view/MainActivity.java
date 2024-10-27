package com.example.sprint1.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprint1.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        byte[] lastFrameData = getIntent().getByteArrayExtra("splash_background");
        if (lastFrameData != null) {
            Bitmap lastFrameBitmap = BitmapFactory.decodeByteArray(
                    lastFrameData, 0, lastFrameData.length);
            getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), lastFrameBitmap));
        }

        // Creating the Sign In button
        Button buttonSignIn = (Button) findViewById(R.id.signIn);
        buttonSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Creating the Sign Up button
        Button buttonSignUp = (Button) findViewById(R.id.signUp);
        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        //creating the quit button
        Button buttonQuit = (Button) findViewById(R.id.quit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }
}