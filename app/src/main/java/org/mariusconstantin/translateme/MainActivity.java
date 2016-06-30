package org.mariusconstantin.translateme;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements Observer<String> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int AUTH_CODE_REQUEST_CODE = 1;
    private TextView mResultsField;
    private EditText mInputField;
    private Button mLaunchTranslateButton;
    private Subscription mSubscription;

    private final TranslationProvider mTranslationProvider = new TranslationProvider();
    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_USE_CREDENTIALS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultsField = (TextView) findViewById(R.id.translated_text_field);
        mInputField = (EditText) findViewById(R.id.input_field);
        mLaunchTranslateButton = (Button) findViewById(R.id.translate_button);

        mLaunchTranslateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 6/23/2016 trigger the translate
            }
        });

        requestPermissions();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.GET_ACCOUNTS)) {
                // TODO: 6/29/2016 Show a toast here

            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);

        } else {
            requestAccounts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // continue with the functionality
                    requestAccounts();
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
            case AUTH_CODE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extra = data.getExtras();
                    String oneTimeToken = extra.getString("authtoken");
                    Log.i(TAG, "onActivityResult: token " + oneTimeToken);
                }
                break;
        }
    }

    private void requestAccounts() {
        AccountManager googleAccountManager = AccountManager.get(this);
        Account[] accounts = googleAccountManager.getAccountsByType("com.google");
        final String[] accountNames = new String[accounts.length];
        if (accounts.length > 0) {
            showChooseAccountDialog(accounts);
        } else {
            showNoAccountAvailableDialog();
        }
    }

    public void showChooseAccountDialog(@NonNull Account[] accounts) {
        final String[] accountNames = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            accountNames[i] = accounts[i].name;
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setItems(accountNames,
                (dialog, which) -> {
                    dialog.dismiss();
                    performRequestToken(accounts[which]);
                }).create();
        alertDialog.show();
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
            return token;
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

    public void showNoAccountAvailableDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.no_valid_account_message))
                .setNeutralButton(getString(R.string.ok_button_label), (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mResultsField = null;
        mInputField = null;
        mLaunchTranslateButton = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    private void translate(final String inputValue) {
        final Observable<String> observable = Observable
                .just(inputValue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(value -> mTranslationProvider.translate(inputValue));
        mSubscription = observable.subscribe(this);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(String s) {
        mResultsField.setText(s);
    }
}
