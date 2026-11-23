package com.dark.desire.dating.network.models;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("userId")
    private String userId;

    @SerializedName("alias")
    private String alias;

    @SerializedName("age")
    private int age;

    @SerializedName("gender")
    private String gender;

    @SerializedName("lookingFor")
    private String lookingFor;

    @SerializedName("bio")
    private String bio;

    @SerializedName("avatarIndex")
    private int avatarIndex;

    public UserProfile() {
    }

    public UserProfile(String userId, String alias, int age, String gender, String lookingFor, String bio, int avatarIndex) {
        this.userId = userId;
        this.alias = alias;
        this.age = age;
        this.gender = gender;
        this.lookingFor = lookingFor;
        this.bio = bio;
        this.avatarIndex = avatarIndex;
    }

    public String getUserId() { return userId; }
    public String getAlias() { return alias; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getLookingFor() { return lookingFor; }
    public String getBio() { return bio; }
    public int getAvatarIndex() { return avatarIndex; }
}
