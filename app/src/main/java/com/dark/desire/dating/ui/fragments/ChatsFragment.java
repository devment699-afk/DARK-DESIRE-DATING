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
import com.dark.desire.dating.ui.adapters.ChatHistoryAdapter;
import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView rvChats;
    private View layoutEmptyChats;
    private ChatHistoryAdapter adapter;
    public static final List<ChatHistoryAdapter.HistoryItem> activeChats = new ArrayList<>();

    static {
        // Sample default conversations
        activeChats.add(new ChatHistoryAdapter.HistoryItem("u_scarlet", "Scarlet_Vixen", "Are you still awake tonight?", "5m ago", "Female", "India 🇮🇳", 22));
        activeChats.add(new ChatHistoryAdapter.HistoryItem("u_velvet", "Velvet_Lips", "Loved our secret conversation earlier...", "42m ago", "Female", "Russia 🇷🇺", 24));
        activeChats.add(new ChatHistoryAdapter.HistoryItem("u_phantom", "Phantom_Wolf", "Let me know when you're free to talk.", "2h ago", "Male", "USA 🇺🇸", 25));
    }

    public static void addOrPromoteChat(UserProfile user, String lastMsg) {
        for (int i = 0; i < activeChats.size(); i++) {
            if (activeChats.get(i).name.equalsIgnoreCase(user.getAlias())) {
                activeChats.remove(i);
                break;
            }
        }
        activeChats.add(0, new ChatHistoryAdapter.HistoryItem(
                user.getUserId(),
                user.getAlias(),
                lastMsg != null ? lastMsg : "Hey, started chatting!",
                "Just now",
                user.getGender(),
                user.getCountry(),
                user.getAge()
        ));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        rvChats = view.findViewById(R.id.rvRecentChats);
        layoutEmptyChats = view.findViewById(R.id.layoutEmptyChats);

        rvChats.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ChatHistoryAdapter(item -> {
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("partner_id", item.userId);
            intent.putExtra("partner_name", item.name);
            intent.putExtra("partner_age", item.age);
            intent.putExtra("partner_country", item.country);
            intent.putExtra("partner_gender", item.gender);
            startActivity(intent);
        });
        rvChats.setAdapter(adapter);

        refreshChatList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshChatList();
    }

    private void refreshChatList() {
        if (activeChats.isEmpty()) {
            layoutEmptyChats.setVisibility(View.VISIBLE);
            rvChats.setVisibility(View.GONE);
        } else {
            layoutEmptyChats.setVisibility(View.GONE);
            rvChats.setVisibility(View.VISIBLE);
            adapter.setItems(activeChats);
        }
    }
}
