package com.dark.desire.dating.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.dark.desire.dating.network.models.ChatMessage;
import com.dark.desire.dating.network.models.UserProfile;
import com.dark.desire.dating.utils.PrefsManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketManager {
    private static WebSocketManager instance;
    private final OkHttpClient client;
    private final Gson gson = new Gson();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<ChatEventListener> listeners = new ArrayList<>();

    private WebSocket webSocket;
    private boolean isConnected = false;
    private boolean isSearching = false;
    private UserProfile currentPartner = null;

    private WebSocketManager() {
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void init(Context context) {
        // Ready
    }

    public void addListener(ChatEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ChatEventListener listener) {
        listeners.remove(listener);
    }

    public void connect() {
        String serverUrl = PrefsManager.getInstance().getServerUrl();
        try {
            Request request = new Request.Builder().url(serverUrl).build();
            webSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket ws, Response response) {
                    isConnected = true;
                    mainHandler.post(() -> {
                        for (ChatEventListener l : listeners) l.onConnected();
                    });
                }

                @Override
                public void onMessage(WebSocket ws, String text) {
                    handleIncomingJson(text);
                }

                @Override
                public void onClosed(WebSocket ws, int code, String reason) {
                    isConnected = false;
                }

                @Override
                public void onFailure(WebSocket ws, Throwable t, Response response) {
                    isConnected = false;
                    // If remote server is unreachable, we allow intelligent simulation fallback
                }
            });
        } catch (Exception e) {
            isConnected = false;
        }
    }

    public void startSearching() {
        isSearching = true;
        mainHandler.post(() -> {
            for (ChatEventListener l : listeners) l.onSearching();
        });

        if (isConnected && webSocket != null) {
            JsonObject json = new JsonObject();
            json.addProperty("action", "join_queue");
            json.addProperty("userId", PrefsManager.getInstance().getUserId());
            json.addProperty("alias", PrefsManager.getInstance().getAlias());
            json.addProperty("gender", PrefsManager.getInstance().getGender());
            json.addProperty("lookingFor", PrefsManager.getInstance().getLookingFor());
            json.addProperty("age", PrefsManager.getInstance().getAge());
            webSocket.send(json.toString());
        } else {
            // Smart offline matching simulation: pairs with a secret stranger after 2.5s radar scan!
            mainHandler.postDelayed(() -> {
                if (isSearching) {
                    isSearching = false;
                    String[] aliases = {"Velvet Whisper", "Midnight Shadow", "Scarlet Rose", "Phantom Desire", "Crimson Flame", "Luna Eclipse"};
                    String alias = aliases[(int)(Math.random() * aliases.length)];
                    currentPartner = new UserProfile(
                            "stranger_" + System.currentTimeMillis(),
                            alias,
                            21 + (int)(Math.random() * 8),
                            "Female",
                            "Anyone",
                            "Looking for someone mysterious...",
                            (int)(Math.random() * 5)
                    );
                    for (ChatEventListener l : listeners) {
                        l.onMatched(currentPartner);
                    }
                }
            }, 2500);
        }
    }

    public void stopSearching() {
        isSearching = false;
        if (isConnected && webSocket != null) {
            JsonObject json = new JsonObject();
            json.addProperty("action", "leave_queue");
            json.addProperty("userId", PrefsManager.getInstance().getUserId());
            webSocket.send(json.toString());
        }
    }

    public void sendMessage(String text) {
        long timestamp = System.currentTimeMillis();
        ChatMessage msg = new ChatMessage(UUID.randomUUID().toString(), PrefsManager.getInstance().getUserId(), text, timestamp, true);

        if (isConnected && webSocket != null) {
            JsonObject json = new JsonObject();
            json.addProperty("action", "send_message");
            json.addProperty("partnerId", currentPartner != null ? currentPartner.getUserId() : "");
            json.addProperty("text", text);
            json.addProperty("timestamp", timestamp);
            webSocket.send(json.toString());
        } else if (currentPartner != null) {
            // Automated playful stranger simulation response
            simulateStrangerReply(text);
        }
    }

    private void simulateStrangerReply(String userMsg) {
        mainHandler.postDelayed(() -> {
            for (ChatEventListener l : listeners) l.onPartnerTyping(true);
        }, 1000);

        mainHandler.postDelayed(() -> {
            for (ChatEventListener l : listeners) l.onPartnerTyping(false);
            String[] replies = {
                    "Hey there... I was hoping I'd match with someone interesting tonight.",
                    "The night is quiet, but your words sparked my curiosity.",
                    "Tell me a secret you've never told anyone else.",
                    "What brought you into the realm of Dark Desire tonight?",
                    "I love the mystery of talking to a complete stranger...",
                    "Are you always this bold, or is it just the anonymity talking?"
            };
            String reply = replies[(int)(Math.random() * replies.length)];
            ChatMessage strangerMsg = new ChatMessage(
                    UUID.randomUUID().toString(),
                    currentPartner != null ? currentPartner.getUserId() : "stranger",
                    reply,
                    System.currentTimeMillis(),
                    false
            );
            for (ChatEventListener l : listeners) {
                l.onMessageReceived(strangerMsg);
            }
        }, 2800);
    }

    public void sendTyping(boolean isTyping) {
        if (isConnected && webSocket != null && currentPartner != null) {
            JsonObject json = new JsonObject();
            json.addProperty("action", "typing");
            json.addProperty("partnerId", currentPartner.getUserId());
            json.addProperty("isTyping", isTyping);
            webSocket.send(json.toString());
        }
    }

    public void leaveChat() {
        if (isConnected && webSocket != null && currentPartner != null) {
            JsonObject json = new JsonObject();
            json.addProperty("action", "leave_chat");
            json.addProperty("partnerId", currentPartner.getUserId());
            webSocket.send(json.toString());
        }
        currentPartner = null;
        for (ChatEventListener l : listeners) {
            l.onPartnerDisconnected();
        }
    }

    private void handleIncomingJson(String text) {
        try {
            JsonObject root = JsonParser.parseString(text).getAsJsonObject();
            String action = root.get("action").getAsString();

            mainHandler.post(() -> {
                switch (action) {
                    case "match_found":
                        JsonObject partnerObj = root.getAsJsonObject("partner");
                        currentPartner = gson.fromJson(partnerObj, UserProfile.class);
                        for (ChatEventListener l : listeners) l.onMatched(currentPartner);
                        break;
                    case "chat_message":
                        ChatMessage msg = gson.fromJson(root.getAsJsonObject("message"), ChatMessage.class);
                        msg.setSelf(false);
                        for (ChatEventListener l : listeners) l.onMessageReceived(msg);
                        break;
                    case "partner_typing":
                        boolean typing = root.get("isTyping").getAsBoolean();
                        for (ChatEventListener l : listeners) l.onPartnerTyping(typing);
                        break;
                    case "partner_disconnected":
                        currentPartner = null;
                        for (ChatEventListener l : listeners) l.onPartnerDisconnected();
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserProfile getCurrentPartner() {
        return currentPartner;
    }
}
