package org.mariusconstantin.translateme;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements Observer<String> {
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
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.GET_ACCOUNTS)) {
                // TODO: 6/29/2016 Show a toast here

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);

            }
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

    private void requestAccounts() {
        AccountManager googleAccountManager = AccountManager.get(this);
        Account[] accounts = googleAccountManager.getAccounts();
        final String[] accountNames = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            accountNames[i] = accounts[i].name;
        }
        showChooseAccountDialog(accountNames);
    }

    private void showChooseAccountDialog(@NonNull String[] accountNames) {
        final AlertDialog alertDialog=new 
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
        mSubscription.unsubscribe();
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
