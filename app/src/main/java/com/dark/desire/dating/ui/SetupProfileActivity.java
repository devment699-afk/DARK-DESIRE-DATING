package com.dark.desire.dating.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dark.desire.dating.R;
import com.dark.desire.dating.utils.PrefsManager;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class SetupProfileActivity extends AppCompatActivity {

    private EditText etAlias, etAge, etBio;
    private MaterialButtonToggleGroup toggleGender, toggleLookingFor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        etAlias = findViewById(R.id.etAlias);
        etAge = findViewById(R.id.etAge);
        etBio = findViewById(R.id.etBio);
        toggleGender = findViewById(R.id.toggleGender);
        toggleLookingFor = findViewById(R.id.toggleLookingFor);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> saveAndEnter());
    }

    private void saveAndEnter() {
        String alias = etAlias.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        if (TextUtils.isEmpty(alias)) {
            etAlias.setError("Enter your secret alias");
            return;
        }

        int age = 22;
        if (!TextUtils.isEmpty(ageStr)) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (Exception ignored) {}
        }

        String gender = "Male";
        int checkedGenderId = toggleGender.getCheckedButtonId();
        if (checkedGenderId == R.id.btnGenderFemale) gender = "Female";
        else if (checkedGenderId == R.id.btnGenderOther) gender = "Other";

        String lookingFor = "Anyone";
        int checkedLookingId = toggleLookingFor.getCheckedButtonId();
        if (checkedLookingId == R.id.btnLookingFemale) lookingFor = "Female";
        else if (checkedLookingId == R.id.btnLookingMale) lookingFor = "Male";

        if (TextUtils.isEmpty(bio)) {
            bio = "Exploring the mysteries of the night...";
        }

        PrefsManager prefs = PrefsManager.getInstance();
        prefs.setAlias(alias);
        prefs.setAge(age);
        prefs.setGender(gender);
        prefs.setLookingFor(lookingFor);
        prefs.setBio(bio);
        prefs.setSetupDone(true);

        Toast.makeText(this, "Welcome to Dark Desire Dating, " + alias, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
