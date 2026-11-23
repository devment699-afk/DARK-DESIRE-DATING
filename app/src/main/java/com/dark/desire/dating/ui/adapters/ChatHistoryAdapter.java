package com.dark.desire.dating.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dark.desire.dating.R;
import java.util.ArrayList;
import java.util.List;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.HistoryViewHolder> {

    public interface OnChatClickListener {
        void onChatClicked(HistoryItem item);
    }

    public static class HistoryItem {
        public String userId;
        public String name;
        public String lastMsg;
        public String time;
        public String gender;
        public String country;
        public int age;

        public HistoryItem(String userId, String name, String lastMsg, String time, String gender, String country, int age) {
            this.userId = userId;
            this.name = name;
            this.lastMsg = lastMsg;
            this.time = time;
            this.gender = gender;
            this.country = country;
            this.age = age;
        }
    }

    private final List<HistoryItem> items = new ArrayList<>();
    private final OnChatClickListener listener;

    public ChatHistoryAdapter(OnChatClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<HistoryItem> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = items.get(position);
        holder.tvName.setText(item.name);
        holder.tvLastMsg.setText(item.lastMsg);
        holder.tvTime.setText(item.time);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onChatClicked(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLastMsg, tvTime;

        HistoryViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvHistoryName);
            tvLastMsg = itemView.findViewById(R.id.tvHistoryLastMsg);
            tvTime = itemView.findViewById(R.id.tvHistoryTime);
        }
    }
}
