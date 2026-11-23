package com.dark.desire.dating.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dark.desire.dating.R;
import com.dark.desire.dating.network.models.ChatMessage;
import com.dark.desire.dating.network.models.UserProfile;
import com.dark.desire.dating.ui.adapters.MessageAdapter;
import com.dark.desire.dating.ui.fragments.ChatsFragment;
import com.google.android.material.button.MaterialButton;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private TextView tvPartnerName, tvPartnerStatus, typingIndicator;
    private RecyclerView rvMessages;
    private MessageAdapter messageAdapter;
    private EditText etMessage;
    private ImageButton btnSend;
    private MaterialButton btnLeaveChat;

    private String partnerId = "stranger_1";
    private String partnerName = "Secret Stranger";
    private String partnerCountry = "India 🇮🇳";
    private String partnerGender = "Female";
    private int partnerAge = 22;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getIntent() != null) {
            String pName = getIntent().getStringExtra("partner_name");
            if (pName != null) partnerName = pName;
            String pId = getIntent().getStringExtra("partner_id");
            if (pId != null) partnerId = pId;
            String pCountry = getIntent().getStringExtra("partner_country");
            if (pCountry != null) partnerCountry = pCountry;
            String pGender = getIntent().getStringExtra("partner_gender");
            if (pGender != null) partnerGender = pGender;
            partnerAge = getIntent().getIntExtra("partner_age", 22);
        }

        tvPartnerName = findViewById(R.id.tvPartnerName);
        tvPartnerStatus = findViewById(R.id.tvPartnerStatus);
        typingIndicator = findViewById(R.id.typingIndicator);
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnLeaveChat = findViewById(R.id.btnLeaveChat);

        // Hide skip button since we're in direct user 1-on-1 chat mode
        View btnNext = findViewById(R.id.btnNextStranger);
        if (btnNext != null) btnNext.setVisibility(View.GONE);

        tvPartnerName.setText(partnerName + " (" + partnerAge + ")");
        tvPartnerStatus.setText("Online • " + partnerCountry);

        messageAdapter = new MessageAdapter();
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(messageAdapter);

        // Welcome opening message from partner
        messageAdapter.addMessage(new ChatMessage(
                UUID.randomUUID().toString(),
                partnerId,
                "Hey... I noticed your presence in the dark desire realm.",
                System.currentTimeMillis() - 60000,
                false
        ));

        btnSend.setOnClickListener(v -> sendMessage());
        btnLeaveChat.setOnClickListener(v -> finish());

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        etMessage.setText("");
        ChatMessage myMsg = new ChatMessage(
                UUID.randomUUID().toString(),
                "me",
                text,
                System.currentTimeMillis(),
                true
        );
        messageAdapter.addMessage(myMsg);
        rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);

        // Update CHAT tab
        UserProfile p = new UserProfile(partnerId, partnerName, partnerAge, partnerGender, partnerCountry, "", 0);
        ChatsFragment.addOrPromoteChat(p, text);

        // Simulate intelligent whisper reply
        handler.postDelayed(() -> typingIndicator.setVisibility(View.VISIBLE), 800);
        handler.postDelayed(() -> {
            typingIndicator.setVisibility(View.GONE);
            String[] replies = {
                    "That made me smile... tell me more.",
                    "You're quite intriguing. Where have you been hiding?",
                    "I love talking to someone who isn't afraid of the shadows.",
                    "What's your biggest fantasy tonight?",
                    "I'm glad you whispered back to me."
            };
            String reply = replies[(int)(Math.random() * replies.length)];
            ChatMessage partnerMsg = new ChatMessage(
                    UUID.randomUUID().toString(),
                    partnerId,
                    reply,
                    System.currentTimeMillis(),
                    false
            );
            messageAdapter.addMessage(partnerMsg);
            rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
            ChatsFragment.addOrPromoteChat(p, reply);
        }, 2400);
    }
}
