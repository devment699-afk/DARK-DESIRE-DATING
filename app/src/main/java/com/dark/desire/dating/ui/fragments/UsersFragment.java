package com.dark.desire.dating.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dark.desire.dating.R;
import com.dark.desire.dating.network.models.UserProfile;
import com.dark.desire.dating.ui.ChatActivity;
import com.dark.desire.dating.ui.adapters.UserCardAdapter;
import com.dark.desire.dating.utils.PrefsManager;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private AppCompatButton btnGenerateUsers;
    private MaterialButtonToggleGroup toggleUserFilter;
    private TextView tvUserCountStatus;
    private RecyclerView rvUsersList;
    private UserCardAdapter adapter;
    private String currentFilter = "ALL";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        btnGenerateUsers = view.findViewById(R.id.btnGenerateUsers);
        toggleUserFilter = view.findViewById(R.id.toggleUserFilter);
        tvUserCountStatus = view.findViewById(R.id.tvUserCountStatus);
        rvUsersList = view.findViewById(R.id.rvUsersList);

        rvUsersList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UserCardAdapter(user -> {
            ChatsFragment.addOrPromoteChat(user, "Hey, I saw your secret profile!");
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("partner_id", user.getUserId());
            intent.putExtra("partner_name", user.getAlias());
            intent.putExtra("partner_age", user.getAge());
            intent.putExtra("partner_country", user.getCountry());
            intent.putExtra("partner_gender", user.getGender());
            startActivity(intent);
        });
        rvUsersList.setAdapter(adapter);

        btnGenerateUsers.setOnClickListener(v -> {
            PrefsManager.getInstance().generateNewLatestUsers();
            Toast.makeText(requireContext(), "⚡ Fresh online users generated!", Toast.LENGTH_SHORT).show();
            loadUsers(currentFilter);
        });

        toggleUserFilter.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            if (checkedId == R.id.btnFilterMales) {
                currentFilter = "MALES";
            } else if (checkedId == R.id.btnFilterFemales) {
                currentFilter = "FEMALES";
            } else {
                currentFilter = "ALL";
            }
            loadUsers(currentFilter);
        });

        loadUsers(currentFilter);
        return view;
    }

    private void loadUsers(String filter) {
        PrefsManager prefs = PrefsManager.getInstance();
        List<UserProfile> list = new ArrayList<>(prefs.getFilteredUsers(filter));

        // Also add the current logged in user to the list if their gender matches the filter
        UserProfile myProfile = prefs.getCurrentUserProfile();
        boolean matchesGender = filter.equalsIgnoreCase("ALL") || 
                (filter.equalsIgnoreCase("MALES") && myProfile.getGender().equalsIgnoreCase("Male")) ||
                (filter.equalsIgnoreCase("FEMALES") && myProfile.getGender().equalsIgnoreCase("Female"));

        if (matchesGender) {
            list.add(0, myProfile);
        }

        adapter.setUsers(list);
        tvUserCountStatus.setText(list.size() + " active users in " + filter + " category");
    }
}
