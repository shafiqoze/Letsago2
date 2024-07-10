package com.example.lab_rest.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lab_rest.model.User;

public class SharePrefManager {
    //the constants
    private static final String SHARED_PREF_NAME = "letsagopref";
    private static final String KEY_ID = "key_id";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_ROLE = "key_role";

    private final Context mCtx;

    public SharePrefManager(Context context) {
        mCtx = context;
    }

    /**
     * method to let the user login
     * this method will store the user data in shared preferences
     * @param user
     */
    public void storeUser(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putString(KEY_ROLE, user.getRole());
        editor.apply();
    }

    /**
     * this method will checker whether user is already logged in or not.
     * return True if already logged in
     */

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    /**
     * this method will give the information of logged in user, retrieved from SharedPreferences
     */
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        User user = new User(
        sharedPreferences.getInt(KEY_ID, -1),
        sharedPreferences.getString(KEY_USERNAME, null),
        sharedPreferences.getString(KEY_EMAIL, null),
        sharedPreferences.getString(KEY_TOKEN, null),
        sharedPreferences.getString(KEY_ROLE, null)
        );
        return user;
    }

    /**
     * this method will logout the user. clear the SharedPreferences
     */
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
