package org.mariusconstantin.translateme.main.inject;

import org.mariusconstantin.translateme.main.translate.TranslateContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 7/27/2016.
 */
@Module
public class MockTranslateModule {
    private final TranslateContract.ITranslateView mTranslateView;

    public MockTranslateModule(TranslateContract.ITranslateView translateView) {
        mTranslateView = translateView;
    }

    @Provides
    public TranslateContract.ITranslateView provideTranslateView() {
        return mTranslateView;
    }
}
