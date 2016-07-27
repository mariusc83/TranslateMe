package org.mariusconstantin.translateme.repositories.translation;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.JsonSyntaxException;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * Created by Marius on 6/23/2016.
 */
public class TranslationRepository {
    private final TranslationNetworkProvider mTranslationProvider;
    private Scheduler mSubscribingScheduler = Schedulers.io();

    public TranslationRepository(TranslationNetworkProvider translationProvider) {
        mTranslationProvider = translationProvider;
    }

    @VisibleForTesting
    TranslationRepository subscribingOn(@NonNull Scheduler scheduler) {
        mSubscribingScheduler = scheduler;
        return this;
    }


    public Observable<TranslationModel> translate(@NonNull String value) {
        return Observable
                .just(value)
                .map(entry -> {
                    try {
                        return mTranslationProvider.translate(entry);
                    } catch (JsonSyntaxException e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .subscribeOn(mSubscribingScheduler)
                .observeOn(AndroidSchedulers.mainThread());

    }
}
