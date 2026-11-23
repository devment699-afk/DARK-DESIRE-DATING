package com.dark.desire.dating.network;

import com.dark.desire.dating.network.models.ChatMessage;
import com.dark.desire.dating.network.models.UserProfile;

public interface ChatEventListener {
    void onConnected();
    void onSearching();
    void onMatched(UserProfile partner);
    void onMessageReceived(ChatMessage message);
    void onPartnerTyping(boolean isTyping);
    void onPartnerDisconnected();
    void onError(String error);
}
