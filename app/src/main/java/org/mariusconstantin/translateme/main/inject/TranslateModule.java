package org.mariusconstantin.translateme.main.inject;

import org.mariusconstantin.translateme.main.translate.TranslateContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 7/26/2016.
 */
@Module
public class TranslateModule {
    private final TranslateContract.ITranslateView mTranslateView;

    public TranslateModule(TranslateContract.ITranslateView translateView) {
        mTranslateView = translateView;
    }

    @Provides
    public TranslateContract.ITranslateView provideTranslateView() {
        return mTranslateView;
    }
}
