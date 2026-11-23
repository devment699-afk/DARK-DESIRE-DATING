package com.dark.desire.dating.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.dark.desire.dating.R;
import com.dark.desire.dating.utils.PrefsManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (PrefsManager.getInstance().isSetupDone()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, SetupProfileActivity.class));
            }
            finish();
        }, 1800);
    }
}
