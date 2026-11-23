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

    @SerializedName("country")
    private String country;

    @SerializedName("bio")
    private String bio;

    @SerializedName("avatarIndex")
    private int avatarIndex;

    @SerializedName("isOnline")
    private boolean isOnline;

    public UserProfile() {
    }

    public UserProfile(String userId, String alias, int age, String gender, String country, String bio, int avatarIndex) {
        this.userId = userId;
        this.alias = alias;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.bio = bio;
        this.avatarIndex = avatarIndex;
        this.isOnline = true;
    }

    public String getUserId() { return userId != null ? userId : alias; }
    public String getAlias() { return alias != null ? alias : "Anonymous"; }
    public int getAge() { return age > 0 ? age : 22; }
    public String getGender() { return gender != null ? gender : "Female"; }
    public String getCountry() { return country != null ? country : "India"; }
    public String getBio() { return bio != null ? bio : "Living in the shadows..."; }
    public int getAvatarIndex() { return avatarIndex; }
    public boolean isOnline() { return isOnline; }

    public void setAlias(String alias) { this.alias = alias; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setCountry(String country) { this.country = country; }
    public void setBio(String bio) { this.bio = bio; }
}
