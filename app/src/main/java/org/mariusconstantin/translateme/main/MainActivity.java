package org.mariusconstantin.translateme.main;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.repositories.translation.TranslationNetworkProvider;
import org.mariusconstantin.translateme.repositories.translation.TranslationRepository;

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

    private final TranslationRepository mTranslationProvider = new TranslationRepository(new TranslationNetworkProvider());
    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_USE_CREDENTIALS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultsField = (TextView) findViewById(R.id.translated_text_field);
        mInputField = (EditText) findViewById(R.id.input_field);
        mLaunchTranslateButton = (Button) findViewById(R.id.translate_button);
        mInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setLaunchButtonStatus(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLaunchTranslateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 6/23/2016 trigger the translate
                translate(mInputField.getText().toString());
            }
        });

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
        final Observable<String> observable = mTranslationProvider.translate(inputValue);
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

    private void setLaunchButtonStatus(@Nullable CharSequence inputText) {
        mLaunchTranslateButton.setEnabled(!TextUtils.isEmpty(inputText));
    }
}
