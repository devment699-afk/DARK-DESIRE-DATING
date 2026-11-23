package com.dark.desire.dating.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.dark.desire.dating.R;
import com.dark.desire.dating.ui.LoginActivity;
import com.dark.desire.dating.utils.PrefsManager;

public class ProfileFragment extends Fragment {

    private TextView tvProfileUsername, tvProfileGender;
    private EditText etProfileAge, etProfileCountry, etProfileBio;
    private AppCompatButton btnSaveProfile;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        tvProfileGender = view.findViewById(R.id.tvProfileGender);
        etProfileAge = view.findViewById(R.id.etProfileAge);
        etProfileCountry = view.findViewById(R.id.etProfileCountry);
        etProfileBio = view.findViewById(R.id.etProfileBio);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        loadProfileData();

        btnSaveProfile.setOnClickListener(v -> saveProfileData());
        btnLogout.setOnClickListener(v -> handleLogout());

        return view;
    }

    private void loadProfileData() {
        PrefsManager prefs = PrefsManager.getInstance();
        tvProfileUsername.setText(prefs.getAlias());

        String gender = prefs.getGender();
        tvProfileGender.setText(gender.equalsIgnoreCase("Male") ? "MALE ♂" : "FEMALE ♀");
        etProfileAge.setText(String.valueOf(prefs.getAge()));
        etProfileCountry.setText(prefs.getCountry());
        etProfileBio.setText(prefs.getBio());
    }

    private void saveProfileData() {
        String ageStr = etProfileAge.getText().toString().trim();
        String country = etProfileCountry.getText().toString().trim();
        String bio = etProfileBio.getText().toString().trim();

        PrefsManager prefs = PrefsManager.getInstance();
        if (!TextUtils.isEmpty(ageStr)) {
            try {
                prefs.setAge(Integer.parseInt(ageStr));
            } catch (Exception ignored) {}
        }
        if (!TextUtils.isEmpty(country)) {
            prefs.setCountry(country);
        }
        if (!TextUtils.isEmpty(bio)) {
            prefs.setBio(bio);
        }

        Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }

    private void handleLogout() {
        // Keeps saved ID and password in PrefsManager so LoginActivity auto-fills for quick re-login!
        PrefsManager.getInstance().logout();
        Toast.makeText(requireContext(), "Logged out. Your credentials remain saved for 1-tap login!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
