package com.dark.desire.dating.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dark.desire.dating.R;
import com.dark.desire.dating.network.models.UserProfile;
import com.dark.desire.dating.ui.ChatActivity;
import com.dark.desire.dating.ui.adapters.UserCardAdapter;
import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {

    private RecyclerView rvDiscover;
    private UserCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        rvDiscover = view.findViewById(R.id.rvDiscoverUsers);
        rvDiscover.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new UserCardAdapter(user -> {
            startActivity(new Intent(requireContext(), ChatActivity.class));
        });
        rvDiscover.setAdapter(adapter);

        loadOnlineUsers();
        return view;
    }

    private void loadOnlineUsers() {
        List<UserProfile> list = new ArrayList<>();
        list.add(new UserProfile("u1", "Scarlet Vixen", 22, "Female", "Male", "Searching for deep late night conversations...", 1));
        list.add(new UserProfile("u2", "Velvet Shadow", 25, "Female", "Anyone", "Mystery is my language. Whisper to me.", 2));
        list.add(new UserProfile("u3", "Midnight Flame", 24, "Male", "Female", "Ready to explore the unknown. No filters.", 3));
        list.add(new UserProfile("u4", "Luna Eclipse", 21, "Female", "Anyone", "Secret desires under the moonlight.", 4));
        list.add(new UserProfile("u5", "Neon Phantom", 26, "Male", "Anyone", "Tell me your real secrets.", 5));
        list.add(new UserProfile("u6", "Crimson Rose", 23, "Female", "Male", "Looking for chemistry and dark aesthetics.", 6));
        adapter.setUsers(list);
    }
}
