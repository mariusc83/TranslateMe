package org.mariusconstantin.translateme.launcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.repositories.GoogleTokenRepo;
import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.ILogger;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by MConstantin on 7/4/2016.
 */
public class LauncherPresenter implements LauncherContract.ILauncherPresenter, Observer<String> {

    private static final String TAG = LauncherPresenter.class.getSimpleName();

    @NonNull
    private final LauncherContract.ILauncherView mView;
    @NonNull
    private final SharedPrefsRepo mSharedPrefsRepo;
    @NonNull
    private final GoogleTokenRepo mGoogleTokenRepo;
    @NonNull
    @AppContext
    private final Context mAppContext;
    @NonNull
    private final ILogger mLogger;

    private Subscription mSubscription;
    private Observable<String> mCachedObservable;
    private boolean mPickAccountsRequested = false;
    private boolean mTokenWasRequested = false;

    @Inject
    public LauncherPresenter(@NonNull @AppContext Context context,
                             @NonNull LauncherContract.ILauncherView launcherView,
                             @NonNull SharedPrefsRepo sharedPrefsRepo,
                             @NonNull GoogleTokenRepo googleTokenRepo,
                             @NonNull ILogger logger) {
        mView = launcherView;
        mSharedPrefsRepo = sharedPrefsRepo;
        mGoogleTokenRepo = googleTokenRepo;
        mAppContext = context;
        mLogger = logger;
    }

    @Override
    public void onStart() {
        if (mSubscription != null && mSubscription.isUnsubscribed() && mCachedObservable != null) {
            if (mTokenWasRequested) {
                mSubscription = mCachedObservable.subscribe(this);
            }
        } else {
            if (mView.checkPermissions() && mView.checkGooglePlayServices())
                checkAccountAndRequestToken();
        }
    }

    @Override
    public void checkAccountAndRequestToken() {
        final String account = getToUseGoogleAccount();
        if (!TextUtils.equals(SharedPrefsRepo.NO_ACCOUNT, account)) {
            requestToken(account);
        } else if (!mPickAccountsRequested) {
            mView.pickAccounts();
            mPickAccountsRequested = true;
        }

    }

    @Override
    public void resetPickAccountsRequested() {
        mPickAccountsRequested = false;
    }

    @Override
    public void resetTokenWasRequested() {
        mTokenWasRequested = false;
    }

    @Override
    public void saveAccount(@NonNull String account) {
        mSharedPrefsRepo.saveSelectedGoogleAccount(account);
    }

    @NonNull
    private String getToUseGoogleAccount() {
        return mSharedPrefsRepo.getStoredSelectedGoogleAccount();
    }

    @Override
    public void requestToken(@NonNull String accountName) {
        if (!mTokenWasRequested) {
            mTokenWasRequested = true;
            mCachedObservable = mGoogleTokenRepo.getToken(accountName,
                    LauncherContract.Scopes.GOOGLE_TRANSLATE_API_SCOPE,
                    mAppContext);
            mSubscription = mCachedObservable.subscribe(this);
        }
    }

    @Override
    public void onStop() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    public void onCompleted() {
        mLogger.d(TAG, "Get Token completed");
        resetPickAccountsRequested();
    }

    @Override
    public void onError(Throwable e) {
        mLogger.e(TAG, "Error while trying to get the Google Api Token.", e);
        mView.handleRequestTokenError(e);
    }

    @Override
    public void onNext(String s) {
        resetPickAccountsRequested();
        saveToken(s);
        mView.goToNextView(s);
    }

    @Override
    public void saveToken(@NonNull String token) {
        // TODO: 7/6/2016 Think about persisting the token here or maybe pass it by
    }

}
