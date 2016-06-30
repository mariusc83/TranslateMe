package org.mariusconstantin.translateme.launcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.repositories.GoogleTokenRepo;
import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by MConstantin on 7/4/2016.
 */
public class LauncherPresenter implements LauncherContract.ILauncherPresenter, Observer<String> {

    @NonNull
    private final LauncherContract.ILauncherView mView;
    @NonNull
    private final SharedPrefsRepo mSharedPrefsRepo;
    @NonNull
    private final GoogleTokenRepo mGoogleTokenRepo;
    @NonNull
    @AppContext
    private final Context mAppContext;

    private Subscription mSubscription;
    private Observable<String> mCachedObservable;


    @Inject
    public LauncherPresenter(@NonNull @AppContext Context context,
                             @NonNull LauncherContract.ILauncherView launcherView,
                             @NonNull SharedPrefsRepo sharedPrefsRepo,
                             @NonNull GoogleTokenRepo googleTokenRepo) {
        mView = launcherView;
        mSharedPrefsRepo = sharedPrefsRepo;
        mGoogleTokenRepo = googleTokenRepo;
        mAppContext = context;
    }

    @Override
    public void onStart() {
        if (mSubscription != null && mSubscription.isUnsubscribed() && mCachedObservable != null) {
            mSubscription = mCachedObservable.subscribe(this);
        } else {
            if (mView.checkPermissions()) {
                if (mView.checkGooglePlayServices()) checkAccountAndRequestToken();
            }
        }
    }

    @Override
    public void checkAccountAndRequestToken() {
        final String account = getToUseGoogleAccount();
        if (!TextUtils.equals(SharedPrefsRepo.NO_ACCOUNT, account)) {
            requestToken(account);
        } else {
            mView.pickAccounts();
        }
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
        mSubscription = mGoogleTokenRepo.getToken(accountName,
                LauncherContract.Scopes.GOOGLE_TRANSLATE_API_SCOPE,
                mAppContext)
                .subscribe(this);
    }

    @Override
    public void onStop() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
    }

    @Override
    public void isActive() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(String s) {

    }
}
