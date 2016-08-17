package org.mariusconstantin.translateme.repositories.misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.inject.AppContext;

import javax.inject.Inject;

/**
 * Created by MConstantin on 7/5/2016.
 */
public class SharedPrefsRepo {

    public static final String NO_ACCOUNT = "no_account";
    @NonNull
    private final SharedPreferences mSharedPreferences;

    private static final String SELECTED_GOOGLE_ACCOUNT_KEY = "selected_google_account";
    private final String sSelectedFromKey;
    private final String sSelectedToKey;


    @Inject
    public SharedPrefsRepo(@AppContext Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sSelectedFromKey = context.getString(R.string.preferred_language_from_key);
        sSelectedToKey = context.getString(R.string.preferred_language_to_key);
    }


    @NonNull
    public String getStoredSelectedGoogleAccount() {
        return mSharedPreferences.getString(SELECTED_GOOGLE_ACCOUNT_KEY, NO_ACCOUNT);

    }

    public void saveSelectedGoogleAccount(@NonNull String account) {
        mSharedPreferences.edit().putString(SELECTED_GOOGLE_ACCOUNT_KEY, account).apply();

    }

    @NonNull
    public String getFromLanguageSelectedCountryValue() {
        if (mSharedPreferences.contains(sSelectedFromKey)) {
            return mSharedPreferences.getString(sSelectedFromKey, "");
        }
        return "";
    }

    @NonNull
    public String getToLanguageSelectedCountryValue() {
        if (mSharedPreferences.contains(sSelectedToKey)) {
            return mSharedPreferences.getString(sSelectedToKey, "");
        }
        return "";
    }
}
