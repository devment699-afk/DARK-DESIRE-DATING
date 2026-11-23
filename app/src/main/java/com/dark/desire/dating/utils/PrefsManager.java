package com.dark.desire.dating.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {
    private static final String PREF_NAME = "dark_desire_prefs";
    private static final String KEY_ALIAS = "alias";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_LOOKING_FOR = "looking_for";
    private static final String KEY_BIO = "bio";
    private static final String KEY_AVATAR_INDEX = "avatar_index";
    private static final String KEY_SERVER_URL = "server_url";
    private static final String KEY_IS_SETUP = "is_setup_done";
    private static final String KEY_USER_ID = "user_id";

    private static PrefsManager instance;
    private final SharedPreferences prefs;

    private PrefsManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new PrefsManager(context);
        }
    }

    public static synchronized PrefsManager getInstance() {
        return instance;
    }

    public boolean isSetupDone() {
        return prefs.getBoolean(KEY_IS_SETUP, false);
    }

    public void setSetupDone(boolean done) {
        prefs.edit().putBoolean(KEY_IS_SETUP, done).apply();
    }

    public String getUserId() {
        String id = prefs.getString(KEY_USER_ID, null);
        if (id == null) {
            id = "user_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
            prefs.edit().putString(KEY_USER_ID, id).apply();
        }
        return id;
    }

    public String getAlias() {
        return prefs.getString(KEY_ALIAS, "Shadow Lover");
    }

    public void setAlias(String alias) {
        prefs.edit().putString(KEY_ALIAS, alias).apply();
    }

    public int getAge() {
        return prefs.getInt(KEY_AGE, 24);
    }

    public void setAge(int age) {
        prefs.edit().putInt(KEY_AGE, age).apply();
    }

    public String getGender() {
        return prefs.getString(KEY_GENDER, "Male");
    }

    public void setGender(String gender) {
        prefs.edit().putString(KEY_GENDER, gender).apply();
    }

    public String getLookingFor() {
        return prefs.getString(KEY_LOOKING_FOR, "Anyone");
    }

    public void setLookingFor(String lookingFor) {
        prefs.edit().putString(KEY_LOOKING_FOR, lookingFor).apply();
    }

    public String getBio() {
        return prefs.getString(KEY_BIO, "Lost in late night thoughts...");
    }

    public void setBio(String bio) {
        prefs.edit().putString(KEY_BIO, bio).apply();
    }

    public int getAvatarIndex() {
        return prefs.getInt(KEY_AVATAR_INDEX, 0);
    }

    public void setAvatarIndex(int index) {
        prefs.edit().putInt(KEY_AVATAR_INDEX, index).apply();
    }

    public String getServerUrl() {
        // Default local/fallback matchmaking server endpoint
        return prefs.getString(KEY_SERVER_URL, "wss://dark-desire-server.onrender.com");
    }

    public void setServerUrl(String url) {
        prefs.edit().putString(KEY_SERVER_URL, url).apply();
    }
}
