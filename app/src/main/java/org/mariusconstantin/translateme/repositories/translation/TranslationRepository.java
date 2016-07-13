package org.mariusconstantin.translateme.repositories.translation;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Marius on 6/23/2016.
 */
public class TranslationRepository {
    private final TranslationNetworkProvider mTranslationProvider;

    public TranslationRepository(TranslationNetworkProvider translationProvider) {
        mTranslationProvider = translationProvider;
    }

    public Observable<String> translate(@NonNull String value) {
        return Observable
                .just(value)
                .map(inputValue -> mTranslationProvider.translate(inputValue))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
