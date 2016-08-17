package org.mariusconstantin.translateme.main.translate;

import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.inject.PerFragment;
import org.mariusconstantin.translateme.main.translate.data.PersistedViewData;
import org.mariusconstantin.translateme.repositories.misc.CountryModel;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;
import org.mariusconstantin.translateme.repositories.translation.TranslatedTextModel;
import org.mariusconstantin.translateme.repositories.translation.TranslationDataModel;
import org.mariusconstantin.translateme.repositories.translation.TranslationModel;
import org.mariusconstantin.translateme.repositories.translation.TranslationRepository;
import org.mariusconstantin.translateme.utils.ILogger;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by MConstantin on 7/26/2016.
 */
public class TranslatePresenter implements TranslateContract.ITranslatePresenter,
        Observer<TranslationModel> {

    private static final String SELECTED_FROM_INDEX_KEY = "f_index";
    private static final String SELECTED_TO_INDEX_KEY = "t_index";
    private static final String PREV_VALUE_KEY = "p_value";

    private static final String TAG = TranslatePresenter.class.getSimpleName();
    @NonNull
    private final TranslationRepository mTranslationRepository;
    @NonNull
    private final ILogger mLogger;
    @NonNull
    private final TranslateContract.ITranslateView mView;
    @NonNull
    private final ILocalesRepository mLocalesRepository;
    @NonNull
    private final PersistedViewData mPersistedViewData;

    private Subscription mSubscription;
    private Observable<TranslationModel> mCachedObservable;


    @PerFragment
    @Inject
    public TranslatePresenter(@NonNull TranslateContract.ITranslateView view,
                              @NonNull TranslationRepository translationRepository,
                              @NonNull ILogger logger,
                              @NonNull ILocalesRepository localesRepository,
                              @NonNull PersistedViewData persistedViewData) {
        mTranslationRepository = translationRepository;
        mLogger = logger;
        mView = view;
        mLocalesRepository = localesRepository;
        mPersistedViewData = persistedViewData;
        mView.setPresenter(this);
    }

    public void translate(@NonNull String toTranslate) {
        mCachedObservable = mTranslationRepository.translate(toTranslate);
        mSubscription = mCachedObservable.subscribe(this);
    }

    @Override
    public void translate(String toTranslate, String fromLang, String toLang) {
        mCachedObservable = mTranslationRepository.translate(toTranslate, fromLang, toLang);
        mSubscription = mCachedObservable.subscribe(this);
    }

    @Override
    public List<CountryModel> getAvailableLanguages() {
        return mLocalesRepository.getSupportedLanguages();
    }

    @Override
    public void onStart() {
        if (mCachedObservable != null) {
            mSubscription = mCachedObservable.subscribe(this);
        }
    }

    @Override
    public void onStop() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onCompleted() {
        mCachedObservable = null;
    }

    @Override
    public void onError(Throwable e) {
        mLogger.e(TAG, e, e.getMessage());
        if (mView.isActive()) {
            mView.displayMessage(R.string.error_occured_while_translating_message);
        }
    }

    @Override
    public void onNext(TranslationModel model) {
        if (!mView.isActive()) return;

        final TranslationDataModel data = model.getData();
        if (data != null) {
            final List<TranslatedTextModel> translatedTexts = data.getTranslations();
            if (!translatedTexts.isEmpty()) {
                mView.displayMessage(translatedTexts.get(0).translatedText());
                return;
            }
        }
        mView.displayMessage(R.string.error_occured_while_translating_message);
    }

    @Override
    public void persistInputText(String s, @NonNull Bundle outState) {
        mPersistedViewData.persistInputText(s, outState);
    }

    @Override
    public void persistToLanguageSpinnerPosition(int selectedItemPosition,
                                                 @NonNull Bundle outState) {
        mPersistedViewData.persistToLanguageIndex(selectedItemPosition, outState);
    }

    @Override
    public void persistFromLanguageSpinnerPosition(int selectedItemPosition,
                                                   @NonNull Bundle outState) {
        mPersistedViewData.persistFromLanguageIndex(selectedItemPosition, outState);
    }

    @Override
    public int getFromLanguageSpinnerIndexDefault(@Nullable Bundle savedState) {
        return mPersistedViewData.getFromLanguageSpinnerIndexDefault(savedState);
    }

    @Override
    public int getToLanguageSpinnerIndexDefault(@Nullable Bundle savedState) {
        return mPersistedViewData.getToLanguageSpinnerIndexDefault(savedState);
    }

    @Override
    public CharSequence getInputTextDefault(@Nullable Bundle savedState) {
        return mPersistedViewData.getInputTextDefault(savedState);
    }

    @Override
    public void trimMemory(int level) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE) {
            mLocalesRepository.clearCache();
        }
    }
}
