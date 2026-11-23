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
        try {
            Request request = new Request.Builder().url("wss://dark-desire-server.onrender.com").build();
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
                }
            });
        } catch (Exception e) {
            isConnected = false;
        }
    }

    public void setCurrentPartner(UserProfile partner) {
        this.currentPartner = partner;
    }

    public UserProfile getCurrentPartner() {
        return currentPartner;
    }

    public void sendMessage(String text) {
        long timestamp = System.currentTimeMillis();
        if (isConnected && webSocket != null && currentPartner != null) {
            JsonObject json = new JsonObject();
            json.addProperty("action", "send_message");
            json.addProperty("partnerId", currentPartner.getUserId());
            json.addProperty("text", text);
            json.addProperty("timestamp", timestamp);
            webSocket.send(json.toString());
        }
    }

    private void handleIncomingJson(String text) {
        try {
            JsonObject root = JsonParser.parseString(text).getAsJsonObject();
            String action = root.get("action").getAsString();

            mainHandler.post(() -> {
                if ("chat_message".equals(action)) {
                    ChatMessage msg = gson.fromJson(root.getAsJsonObject("message"), ChatMessage.class);
                    msg.setSelf(false);
                    for (ChatEventListener l : listeners) l.onMessageReceived(msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
