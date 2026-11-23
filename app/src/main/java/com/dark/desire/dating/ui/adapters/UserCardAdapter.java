package com.dark.desire.dating.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dark.desire.dating.R;
import com.dark.desire.dating.network.models.UserProfile;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserViewHolder> {

    public interface OnUserChatClickListener {
        void onChatClicked(UserProfile user);
    }

    private final List<UserProfile> users = new ArrayList<>();
    private final OnUserChatClickListener listener;

    public UserCardAdapter(OnUserChatClickListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<UserProfile> list) {
        users.clear();
        users.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserProfile user = users.get(position);
        holder.tvAlias.setText(user.getAlias());
        holder.tvAgeGender.setText(user.getAge() + " • " + (user.getGender().equalsIgnoreCase("Female") ? "F" : "M"));
        holder.tvBio.setText(user.getBio());
        holder.btnChat.setOnClickListener(v -> {
            if (listener != null) listener.onChatClicked(user);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlias, tvAgeGender, tvBio;
        MaterialButton btnChat;

        UserViewHolder(View itemView) {
            super(itemView);
            tvAlias = itemView.findViewById(R.id.tvCardAlias);
            tvAgeGender = itemView.findViewById(R.id.tvCardAgeGender);
            tvBio = itemView.findViewById(R.id.tvCardBio);
            btnChat = itemView.findViewById(R.id.btnCardChat);
        }
    }
}
