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

        String genderLabel = user.getGender().equalsIgnoreCase("Male") ? "Male ♂" : "Female ♀";
        holder.tvGender.setText(genderLabel);
        holder.tvAgeCountry.setText(user.getAge() + " yrs • " + user.getCountry());
        holder.tvBio.setText(user.getBio());

        holder.btnCardChat.setOnClickListener(v -> {
            if (listener != null) listener.onChatClicked(user);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlias, tvGender, tvAgeCountry, tvBio;
        MaterialButton btnCardChat;

        UserViewHolder(View itemView) {
            super(itemView);
            tvAlias = itemView.findViewById(R.id.tvCardAlias);
            tvGender = itemView.findViewById(R.id.tvCardGender);
            tvAgeCountry = itemView.findViewById(R.id.tvCardAgeCountry);
            tvBio = itemView.findViewById(R.id.tvCardBio);
            btnCardChat = itemView.findViewById(R.id.btnCardChat);
        }
    }
}
