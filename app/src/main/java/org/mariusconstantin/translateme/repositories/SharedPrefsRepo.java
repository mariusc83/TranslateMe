package org.mariusconstantin.translateme.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.AppContext;

import javax.inject.Inject;

/**
 * Created by MConstantin on 7/5/2016.
 */
public class SharedPrefsRepo {

    private static final String SHARED_PREFS_NAME = "main_shared_prefs";

    public static final String NO_ACCOUNT = "no_account";
    @NonNull
    private final SharedPreferences mSharedPreferences;

    private static final String SELECTED_GOOGLE_ACCOUNT_KEY = "selected_google_account";


    @Inject
    public SharedPrefsRepo(@AppContext Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }


    @NonNull
    public String getStoredSelectedGoogleAccount() {
        return mSharedPreferences.getString(SELECTED_GOOGLE_ACCOUNT_KEY, NO_ACCOUNT);

    }

    public void saveSelectedGoogleAccount(@NonNull String account) {
        mSharedPreferences.edit().putString(SELECTED_GOOGLE_ACCOUNT_KEY, account).apply();

    }
}
