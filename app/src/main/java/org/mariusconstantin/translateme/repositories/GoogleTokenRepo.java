package org.mariusconstantin.translateme.repositories;

import android.accounts.Account;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import org.mariusconstantin.translateme.inject.AppContext;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * Created by MConstantin on 7/5/2016.
 */
public class GoogleTokenRepo {
    private static final String GOOGLE_ACCOUNT_TYPE = "com.google";

    public Observable<String> getToken(@NonNull String accountName, @NonNull String scope, @AppContext Context context) {
        return Observable
                .just(accountName)
                .map(account -> {
                    try {
                        return GoogleAuthUtil.getToken(context, new Account(account, GOOGLE_ACCOUNT_TYPE), scope);
                    } catch (IOException | GoogleAuthException e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
