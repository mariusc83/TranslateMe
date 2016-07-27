package org.mariusconstantin.translateme.main.translate;

import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.R;
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
    private static final String TAG = TranslatePresenter.class.getSimpleName();
    @NonNull
    private final TranslationRepository mTranslationRepository;
    @NonNull
    private final ILogger mLogger;
    @NonNull
    private final TranslateContract.ITranslateView mView;

    private Subscription mSubscription;
    private Observable<TranslationModel> mCachedObservable;

    @Inject
    public TranslatePresenter(@NonNull TranslateContract.ITranslateView view,
                              @NonNull TranslationRepository translationRepository,
                              @NonNull ILogger logger) {
        mTranslationRepository = translationRepository;
        mLogger = logger;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void translate(@NonNull String toTranslate) {
        mCachedObservable = mTranslationRepository.translate(toTranslate);
        mSubscription = mCachedObservable.subscribe(this);
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
}
