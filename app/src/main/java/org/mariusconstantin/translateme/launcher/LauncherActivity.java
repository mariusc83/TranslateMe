package org.mariusconstantin.translateme.launcher;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;

import org.mariusconstantin.translateme.BaseActivity;
import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.launcher.inject.DaggerLauncherComponent;
import org.mariusconstantin.translateme.launcher.inject.LauncherComponent;
import org.mariusconstantin.translateme.launcher.inject.LauncherModule;
import org.mariusconstantin.translateme.main.MainActivity;
import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.AppUtils;

import java.io.IOException;

/**
 * Created by MConstantin on 6/30/2016.
 */
public class LauncherActivity extends BaseActivity implements LauncherContract.ILauncherView {

    private static final String SELECTED_MAIL_ACCOUNT_KEY = "mail_account";
    private static final String MAIN_SHARED_PREFS = "main_shared_prefs";
    private static final String ACCOUNT_EMPTY = "";

    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 1;
    private final static String TRANSLATE_API_SCOPE
            = "https://www.googleapis.com/auth/translate";
    private final static String mScopes
            = "oauth2:" + TRANSLATE_API_SCOPE;

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1;
    private static final int AUTH_CODE_REQUEST_CODE = 2;
    private static final String ACCOUNT_TYPES = "com.google";
    private static final String TAG = LauncherActivity.class.getSimpleName();

    private LauncherContract.ILauncherPresenter mLauncherPresenter;

    private AppUtils mAppUtils;

    private Context mAppContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        LauncherComponent component = DaggerLauncherComponent.builder().launcherModule(new LauncherModule(this)).build();
        mLauncherPresenter = component.getLauncherPresenter();
        mAppContext = component.getAppContext();
        mAppUtils = component.getAppUtils();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLauncherPresenter.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLauncherPresenter.onStop();
    }

    @Override
    public boolean checkGooglePlayServices() {
        int status = mAppUtils.isGooglePlayServiceAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (mAppUtils.isRecoverable(status)) {
                mAppUtils.getErrorDialog(status, this).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void pickAccounts() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }


    @Override
    public boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.GET_ACCOUNTS)) {
                Snackbar.make(getRootView(),
                        R.string.permission_required_message, Snackbar.LENGTH_LONG).show();

            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);

            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLauncherPresenter.checkAccountAndRequestToken();
                } else {
                    // TODO: 6/29/2016 Show alert dialog here
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_ACCOUNT:
                if (resultCode == RESULT_OK) {
                    final String account = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    mLauncherPresenter.saveAccount(account);
                    mLauncherPresenter.requestToken(account);
                } else {
                    Snackbar.make(getRootView(), R.string.select_account_to_continue_dialog_message, Snackbar.LENGTH_LONG).show();
                }
                break;
            case AUTH_CODE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extra = data.getExtras();
                    String oneTimeToken = extra.getString("authtoken");
                }
                break;
        }
    }

    private View getRootView() {
        return findViewById(R.id.root_container);
    }

    private String getMailAccount() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(SELECTED_MAIL_ACCOUNT_KEY, ACCOUNT_EMPTY);
    }

    private void performRequestToken(Account account) {
        new Thread(() -> {
            try {
                requestToken(account);
            } catch (IOException e) {
                Log.e(TAG, "an exception occurred :  ", e);
            }
        }).start();
    }


    private String requestToken(Account account) throws IOException {
        try {
            final String token = GoogleAuthUtil.getToken(this, account, "oauth2:https://www.googleapis.com/auth/translate");
            Log.d(TAG, "requestToken() called with: " + "account = [" + account + "] and got the token = [" + token + "]");
            // go to MainActivity. If we need the token we will pass it here
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        } catch (UserRecoverableAuthException userRecoverableException) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            userRecoverableException.printStackTrace();
            startActivityForResult(userRecoverableException.getIntent(), AUTH_CODE_REQUEST_CODE, null);

        } catch (GoogleAuthException e) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.
            e.printStackTrace();
        }

        return null;
    }

}
