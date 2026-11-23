package com.dark.desire.dating.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dark.desire.dating.R;
import com.dark.desire.dating.network.ChatEventListener;
import com.dark.desire.dating.network.WebSocketManager;
import com.dark.desire.dating.network.models.ChatMessage;
import com.dark.desire.dating.network.models.UserProfile;
import com.dark.desire.dating.ui.adapters.MessageAdapter;
import com.google.android.material.button.MaterialButton;

public class ChatActivity extends AppCompatActivity implements ChatEventListener {

    private TextView tvPartnerName, tvPartnerStatus, typingIndicator, tvDisconnectedBanner;
    private RecyclerView rvMessages;
    private MessageAdapter messageAdapter;
    private EditText etMessage;
    private ImageButton btnSend;
    private MaterialButton btnNextStranger, btnLeaveChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tvPartnerName = findViewById(R.id.tvPartnerName);
        tvPartnerStatus = findViewById(R.id.tvPartnerStatus);
        typingIndicator = findViewById(R.id.typingIndicator);
        tvDisconnectedBanner = findViewById(R.id.tvDisconnectedBanner);
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnNextStranger = findViewById(R.id.btnNextStranger);
        btnLeaveChat = findViewById(R.id.btnLeaveChat);

        messageAdapter = new MessageAdapter();
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(messageAdapter);

        UserProfile partner = WebSocketManager.getInstance().getCurrentPartner();
        if (partner != null) {
            tvPartnerName.setText(partner.getAlias() + " (" + partner.getAge() + ")");
            tvPartnerStatus.setText("Connected");
        }

        btnSend.setOnClickListener(v -> sendMessage());

        btnNextStranger.setOnClickListener(v -> {
            WebSocketManager.getInstance().leaveChat();
            messageAdapter.clear();
            tvDisconnectedBanner.setVisibility(View.GONE);
            tvPartnerStatus.setText("Searching next stranger...");
            WebSocketManager.getInstance().startSearching();
        });

        btnLeaveChat.setOnClickListener(v -> {
            WebSocketManager.getInstance().leaveChat();
            finish();
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                WebSocketManager.getInstance().sendTyping(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        WebSocketManager.getInstance().addListener(this);
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        etMessage.setText("");
        messageAdapter.addMessage(new ChatMessage(
                "msg_" + System.currentTimeMillis(),
                "me",
                text,
                System.currentTimeMillis(),
                true
        ));
        rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
        WebSocketManager.getInstance().sendMessage(text);
    }

    @Override
    public void onConnected() {}

    @Override
    public void onSearching() {
        tvPartnerStatus.setText("Matching in progress...");
    }

    @Override
    public void onMatched(UserProfile partner) {
        tvDisconnectedBanner.setVisibility(View.GONE);
        tvPartnerName.setText(partner.getAlias() + " (" + partner.getAge() + ")");
        tvPartnerStatus.setText("Connected");
        Toast.makeText(this, "Matched with " + partner.getAlias(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessageReceived(ChatMessage message) {
        messageAdapter.addMessage(message);
        rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    @Override
    public void onPartnerTyping(boolean isTyping) {
        typingIndicator.setVisibility(isTyping ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPartnerDisconnected() {
        tvDisconnectedBanner.setVisibility(View.VISIBLE);
        tvPartnerStatus.setText("Disconnected");
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().removeListener(this);
    }
}
