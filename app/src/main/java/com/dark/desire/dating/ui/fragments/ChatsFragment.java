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
import com.dark.desire.dating.ui.ChatActivity;
import com.dark.desire.dating.ui.adapters.ChatHistoryAdapter;
import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView rvChats;
    private ChatHistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        rvChats = view.findViewById(R.id.rvRecentChats);
        rvChats.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ChatHistoryAdapter();
        rvChats.setAdapter(adapter);

        loadRecentChats();
        return view;
    }

    private void loadRecentChats() {
        List<ChatHistoryAdapter.HistoryItem> list = new ArrayList<>();
        list.add(new ChatHistoryAdapter.HistoryItem("Secret Stranger #4928", "I never thought we would connect like this...", "12m ago"));
        list.add(new ChatHistoryAdapter.HistoryItem("Velvet Whisper", "Will you be here tomorrow night?", "2h ago"));
        list.add(new ChatHistoryAdapter.HistoryItem("Phantom Soul", "Disconnected by partner", "Yesterday"));
        adapter.setItems(list);
    }
}
