package com.example.lab_rest.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lab_rest.model.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "bookstoresharedpref";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_ROLE = "keyrole";
    private static final String KEY_TOKEN = "keytoken";

    private static SharedPrefManager instance;
    private static Context ctx;

    public SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putString(KEY_TOKEN, user.getToken());

        editor.apply();
    }

    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        User user = new User();
        user.setId(sharedPreferences.getInt(KEY_ID, -1));
        user.setUsername(sharedPreferences.getString(KEY_USERNAME, null));
        user.setRole(sharedPreferences.getString(KEY_ROLE, null));
        user.setToken(sharedPreferences.getString(KEY_TOKEN, null));
        return user;
    }

    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }
}
