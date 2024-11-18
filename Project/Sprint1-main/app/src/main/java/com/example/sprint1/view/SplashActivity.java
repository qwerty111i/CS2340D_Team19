package com.example.sprint1.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.example.sprint1.R;
import com.example.sprint1.model.CommunityModel;

import java.io.ByteArrayOutputStream;

public class SplashActivity extends AppCompatActivity {
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Setting up size for travelformentry
        CommunityModel.getInstance().addSampleIfEmpty();

        lottieAnimationView = findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation(R.raw.splash_animation);

        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Bitmap lastFrame = Bitmap.createBitmap(
                        lottieAnimationView.getWidth(),
                        lottieAnimationView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(lastFrame);
                lottieAnimationView.draw(canvas);

                byte[] lastFrameData = bitmapToByteArray(lastFrame);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("splash_background", lastFrameData);
                startActivity(intent);
                finish();
            }
        });

        lottieAnimationView.playAnimation();

        //creating a handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 7000); //we can manually change the delay here
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}