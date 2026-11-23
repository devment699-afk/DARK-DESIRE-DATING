package com.dark.desire.dating.network.models;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    @SerializedName("id")
    private String id;

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("text")
    private String text;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("isSelf")
    private boolean isSelf;

    public ChatMessage() {
    }

    public ChatMessage(String id, String senderId, String text, long timestamp, boolean isSelf) {
        this.id = id;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
        this.isSelf = isSelf;
    }

    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
    public boolean isSelf() { return isSelf; }

    public void setSelf(boolean self) { isSelf = self; }
}
