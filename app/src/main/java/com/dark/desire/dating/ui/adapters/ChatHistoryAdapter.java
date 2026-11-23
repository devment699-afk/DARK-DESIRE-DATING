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

    public static class HistoryItem {
        public String name;
        public String lastMsg;
        public String time;

        public HistoryItem(String name, String lastMsg, String time) {
            this.name = name;
            this.lastMsg = lastMsg;
            this.time = time;
        }
    }

    private final List<HistoryItem> items = new ArrayList<>();

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
