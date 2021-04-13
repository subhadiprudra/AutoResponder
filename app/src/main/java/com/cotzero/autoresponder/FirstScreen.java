package com.cotzero.autoresponder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;

public class FirstScreen extends AppCompatActivity {

    Button button;
    ProgressBar progressBar;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        button = findViewById(R.id.permission);
        progressBar= findViewById(R.id.progress_circular);
        lottieAnimationView = findViewById(R.id.lottie_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        });

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("Animation:","start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(FirstScreen.this,MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:","cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:","repeat");
            }
        });


    }

    Boolean shouldRun=true;
    @Override
    protected void onResume() {
        super.onResume();



        String enabledNotificationListeners = Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners");
        String packageName = this.getPackageName();


        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName))
        {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            lottieAnimationView.loop(true);
        }
        else {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            lottieAnimationView.loop(false);
        }
    }
}