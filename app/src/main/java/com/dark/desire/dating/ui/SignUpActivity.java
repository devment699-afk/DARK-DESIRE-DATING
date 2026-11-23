package com.dark.desire.dating.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.dark.desire.dating.R;
import com.dark.desire.dating.utils.PrefsManager;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class SignUpActivity extends AppCompatActivity {

    private EditText etSignUpUsername, etSignUpPassword, etSignUpAge, etSignUpCountry, etSignUpBio;
    private MaterialButtonToggleGroup toggleSignUpGender;
    private AppCompatButton btnSignUpSubmit;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etSignUpUsername = findViewById(R.id.etSignUpUsername);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etSignUpAge = findViewById(R.id.etSignUpAge);
        etSignUpCountry = findViewById(R.id.etSignUpCountry);
        etSignUpBio = findViewById(R.id.etSignUpBio);
        toggleSignUpGender = findViewById(R.id.toggleSignUpGender);
        btnSignUpSubmit = findViewById(R.id.btnSignUpSubmit);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnSignUpSubmit.setOnClickListener(v -> handleSignUp());
        tvGoToLogin.setOnClickListener(v -> finish());
    }

    private void handleSignUp() {
        String username = etSignUpUsername.getText().toString().trim();
        String password = etSignUpPassword.getText().toString().trim();
        String ageStr = etSignUpAge.getText().toString().trim();
        String country = etSignUpCountry.getText().toString().trim();
        String bio = etSignUpBio.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etSignUpUsername.setError("Username required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etSignUpPassword.setError("Password required");
            return;
        }

        String gender = "Male";
        int checkedId = toggleSignUpGender.getCheckedButtonId();
        if (checkedId == R.id.btnSelectFemale) {
            gender = "Female";
        }

        int age = 22;
        if (!TextUtils.isEmpty(ageStr)) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (Exception ignored) {}
        }

        if (TextUtils.isEmpty(country)) {
            country = "India 🇮🇳";
        }

        if (TextUtils.isEmpty(bio)) {
            bio = "Exploring the mysteries of the night...";
        }

        PrefsManager prefs = PrefsManager.getInstance();
        prefs.saveUserAccount(username, password, gender, age, country, bio);

        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }
}
