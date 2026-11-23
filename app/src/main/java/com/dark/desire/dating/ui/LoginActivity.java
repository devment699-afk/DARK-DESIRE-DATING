package com.dark.desire.dating.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.dark.desire.dating.R;
import com.dark.desire.dating.utils.PrefsManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginUsername, etLoginPassword;
    private CheckBox cbRememberMe;
    private AppCompatButton btnLogin;
    private TextView tvGoToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToSignUp = findViewById(R.id.tvGoToSignUp);

        // Auto-fill saved credentials so user can log in with 1 tap if they previously logged out!
        String savedUser = PrefsManager.getInstance().getSavedUsername();
        String savedPass = PrefsManager.getInstance().getSavedPassword();
        if (!TextUtils.isEmpty(savedUser)) {
            etLoginUsername.setText(savedUser);
        }
        if (!TextUtils.isEmpty(savedPass)) {
            etLoginPassword.setText(savedPass);
        }

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvGoToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    private void attemptLogin() {
        String username = etLoginUsername.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etLoginUsername.setError("Please enter your username");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etLoginPassword.setError("Please enter your password");
            return;
        }

        PrefsManager prefs = PrefsManager.getInstance();
        if (cbRememberMe.isChecked()) {
            prefs.setSavedUsername(username);
            prefs.setSavedPassword(password);
        }

        prefs.setAlias(username);
        prefs.setLoggedIn(true);

        Toast.makeText(this, "Welcome back, " + username + "!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
