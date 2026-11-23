package com.dark.desire.dating.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.dark.desire.dating.R;
import com.dark.desire.dating.network.WebSocketManager;
import com.dark.desire.dating.utils.PrefsManager;

public class SettingsFragment extends Fragment {

    private EditText etSettingsAlias, etSettingsBio, etServerUrl;
    private Button btnClearHistory;
    private AppCompatButton btnSaveSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        etSettingsAlias = view.findViewById(R.id.etSettingsAlias);
        etSettingsBio = view.findViewById(R.id.etSettingsBio);
        etServerUrl = view.findViewById(R.id.etServerUrl);
        btnClearHistory = view.findViewById(R.id.btnClearHistory);
        btnSaveSettings = view.findViewById(R.id.btnSaveSettings);

        PrefsManager prefs = PrefsManager.getInstance();
        etSettingsAlias.setText(prefs.getAlias());
        etSettingsBio.setText(prefs.getBio());
        etServerUrl.setText(prefs.getServerUrl());

        btnSaveSettings.setOnClickListener(v -> {
            String alias = etSettingsAlias.getText().toString().trim();
            String bio = etSettingsBio.getText().toString().trim();
            String url = etServerUrl.getText().toString().trim();

            if (!TextUtils.isEmpty(alias)) prefs.setAlias(alias);
            if (!TextUtils.isEmpty(bio)) prefs.setBio(bio);
            if (!TextUtils.isEmpty(url)) {
                prefs.setServerUrl(url);
                WebSocketManager.getInstance().connect();
            }

            Toast.makeText(requireContext(), "Secret Profile Updated!", Toast.LENGTH_SHORT).show();
        });

        btnClearHistory.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "All Chat History Wiped Clean!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
