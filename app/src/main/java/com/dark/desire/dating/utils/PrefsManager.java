package com.dark.desire.dating.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.dark.desire.dating.network.models.UserProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrefsManager {
    private static final String PREF_NAME = "dark_desire_prefs";
    private static final String KEY_LOGGED_IN = "is_logged_in";
    private static final String KEY_SAVED_USERNAME = "saved_username";
    private static final String KEY_SAVED_PASSWORD = "saved_password";

    private static final String KEY_ALIAS = "alias";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_BIO = "bio";
    private static final String KEY_AVATAR_INDEX = "avatar_index";

    private static final String KEY_STORED_USERS = "stored_users_json";
    private static final String KEY_CHATS_HISTORY = "chats_history_json";

    private static PrefsManager instance;
    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

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

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        prefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply();
    }

    public String getSavedUsername() {
        return prefs.getString(KEY_SAVED_USERNAME, "");
    }

    public void setSavedUsername(String username) {
        prefs.edit().putString(KEY_SAVED_USERNAME, username).apply();
    }

    public String getSavedPassword() {
        return prefs.getString(KEY_SAVED_PASSWORD, "");
    }

    public void setSavedPassword(String password) {
        prefs.edit().putString(KEY_SAVED_PASSWORD, password).apply();
    }

    public void saveUserAccount(String username, String password, String gender, int age, String country, String bio) {
        prefs.edit()
                .putString(KEY_SAVED_USERNAME, username)
                .putString(KEY_SAVED_PASSWORD, password)
                .putString(KEY_ALIAS, username)
                .putString(KEY_GENDER, gender)
                .putInt(KEY_AGE, age)
                .putString(KEY_COUNTRY, country)
                .putString(KEY_BIO, bio)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public void logout() {
        // Keep saved credentials intact so 1-tap re-login works smoothly!
        prefs.edit().putBoolean(KEY_LOGGED_IN, false).apply();
    }

    public String getAlias() {
        return prefs.getString(KEY_ALIAS, getSavedUsername().isEmpty() ? "Shadow Desire" : getSavedUsername());
    }

    public void setAlias(String alias) {
        prefs.edit().putString(KEY_ALIAS, alias).apply();
    }

    public int getAge() {
        return prefs.getInt(KEY_AGE, 23);
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

    public String getCountry() {
        return prefs.getString(KEY_COUNTRY, "India");
    }

    public void setCountry(String country) {
        prefs.edit().putString(KEY_COUNTRY, country).apply();
    }

    public String getBio() {
        return prefs.getString(KEY_BIO, "Exploring secret late-night desires...");
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

    public UserProfile getCurrentUserProfile() {
        return new UserProfile(
                getAlias(),
                getAlias(),
                getAge(),
                getGender(),
                getCountry(),
                getBio(),
                getAvatarIndex()
        );
    }

    // Dynamic Users Generator
    public List<UserProfile> getStoredUsers() {
        String json = prefs.getString(KEY_STORED_USERS, null);
        if (json == null) {
            List<UserProfile> initial = generateDefaultUsers();
            saveStoredUsers(initial);
            return initial;
        }
        Type type = new TypeToken<List<UserProfile>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveStoredUsers(List<UserProfile> list) {
        prefs.edit().putString(KEY_STORED_USERS, gson.toJson(list)).apply();
    }

    public List<UserProfile> generateNewLatestUsers() {
        List<UserProfile> list = generateDefaultUsers();
        saveStoredUsers(list);
        return list;
    }

    private List<UserProfile> generateDefaultUsers() {
        List<UserProfile> list = new ArrayList<>();
        Random r = new Random();

        // Female profiles
        String[] femaleNames = {"Scarlet_Vixen", "Velvet_Lips", "Mystic_Rose", "Luna_Eclipse", "Zara_Desire", "Ruby_Flame", "Angel_Eyes", "Midnight_Queen"};
        String[] femaleBios = {
                "Looking for a real connection in the dark...",
                "Whisper your deepest secrets to me.",
                "Late night conversations hit different.",
                "Bold, spontaneous and unapologetic.",
                "Here for chemistry and deep conversations."
        };

        // Male profiles
        String[] maleNames = {"Shadow_Rebel", "Phantom_Wolf", "Aryan_Dark", "Lucifer_Knight", "Vikram_Desire", "Neon_Rider", "Dark_Knight", "Phoenix_Flame"};
        String[] maleBios = {
                "Seeking someone captivating to talk to.",
                "Mystery is my vibe. No drama.",
                "Here to listen and explore passions.",
                "Midnight traveler looking for deep chats.",
                "Straightforward, loyal and adventurous."
        };

        String[] countries = {"India 🇮🇳", "USA 🇺🇸", "Russia 🇷🇺", "Brazil 🇧🇷", "UK 🇬🇧", "Germany 🇩🇪", "Canada 🇨🇦", "UAE 🇦🇪"};

        for (int i = 0; i < femaleNames.length; i++) {
            list.add(new UserProfile(
                    "f_" + i,
                    femaleNames[i],
                    19 + r.nextInt(9),
                    "Female",
                    countries[r.nextInt(countries.length)],
                    femaleBios[r.nextInt(femaleBios.length)],
                    i % 6
            ));
        }

        for (int i = 0; i < maleNames.length; i++) {
            list.add(new UserProfile(
                    "m_" + i,
                    maleNames[i],
                    21 + r.nextInt(10),
                    "Male",
                    countries[r.nextInt(countries.length)],
                    maleBios[r.nextInt(maleBios.length)],
                    i % 6
            ));
        }

        return list;
    }

    // Filter Users: strictly "MALES", "FEMALES", "ALL"
    public List<UserProfile> getFilteredUsers(String filter) {
        List<UserProfile> all = getStoredUsers();
        if (filter.equalsIgnoreCase("MALES")) {
            List<UserProfile> males = new ArrayList<>();
            for (UserProfile u : all) {
                if (u.getGender().equalsIgnoreCase("Male")) {
                    males.add(u);
                }
            }
            return males;
        } else if (filter.equalsIgnoreCase("FEMALES")) {
            List<UserProfile> females = new ArrayList<>();
            for (UserProfile u : all) {
                if (u.getGender().equalsIgnoreCase("Female")) {
                    females.add(u);
                }
            }
            return females;
        } else {
            return all; // ALL USERS
        }
    }
}
