package com.levibostian.androidblanky.manager;

import android.content.Context;
import android.content.SharedPreferences;
import com.levibostian.androidblanky.R;

import javax.inject.Inject;

public class UserManager extends SharedPreferencesManager {

    private static SharedPreferences sSharedPreferences;
    private static Context sContext;

    @Inject
    public UserManager(Context context, SharedPreferences sharedPreferences) {
        super(context, sharedPreferences);

        sSharedPreferences = sharedPreferences;
        sContext = context;
    }

//    public boolean isUserLoggedIn() {
//        return getClientId() != INVALID_ID &&
//               getFirstName() != null &&
//               getLastName() != null &&
//               getAccessToken() != null;
//    }

//    public int getClientId() {
//        return getIntFromSharedPreferences(R.string.preferences_client_id);
//    }
//
//    public String getFirstName() {
//        return getStringFromSharedPreferences(R.string.preferences_client_first_name);
//    }
//
//    public String getLastName() {
//        return getStringFromSharedPreferences(R.string.preferences_client_last_name);
//    }
//
//    public String getAccessToken() {
//        return getStringFromSharedPreferences(R.string.preferences_client_access_token);
//    }

    public static class Editor {

        private int mId = INVALID_ID;
        public String mFirstName;
        public String mLastName;
        public String mAccessToken;

        public Editor() {
        }

        public Editor setId(int id) {
            mId = id;

            return this;
        }

        public Editor setFirstName(String firstName) {
            mFirstName = firstName;

            return this;
        }

        public Editor setLastName(String lastName) {
            mLastName = lastName;

            return this;
        }

        public Editor setAccessToken(String accessToken) {
            mAccessToken = accessToken;

            return this;
        }

        public void commit() {
            if (mId == INVALID_ID || mFirstName == null || mLastName == null || mAccessToken == null) {
                throw new RuntimeException("You need to set all fields for user before committing to storage.");
            } else {
                SharedPreferences.Editor editor = sSharedPreferences.edit();
                //editor.putInt(sContext.getString(R.string.preferences_client_id), mId);
                //editor.putString(sContext.getString(R.string.preferences_client_first_name), mFirstName);
                //editor.putString(sContext.getString(R.string.preferences_client_last_name), mLastName);
                //editor.putString(sContext.getString(R.string.preferences_client_access_token), mAccessToken);
                editor.commit();
            }
        }
    }

}
