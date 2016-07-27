package org.mariusconstantin.translateme.main.inject;

import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.PerActivity;
import org.mariusconstantin.translateme.repositories.translation.TranslationNetworkProvider;
import org.mariusconstantin.translateme.repositories.translation.TranslationRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 7/27/2016.
 */
@Module
public class MockMainModule {
    @NonNull
    private final TranslationNetworkProvider mNetworkProvider;

    @NonNull
    private final TranslationRepository mTranslationRepository;

    public MockMainModule(@NonNull TranslationNetworkProvider networkProvider,
                          @NonNull TranslationRepository translationRepository) {
        mNetworkProvider = networkProvider;
        mTranslationRepository = translationRepository;
    }

    @Provides
    public TranslationNetworkProvider provideTranslationNetworkProvider() {
        return mNetworkProvider;
    }

    @PerActivity
    @Provides
    public TranslationRepository provideTranslationRepository(
            @NonNull TranslationNetworkProvider networkProvider) {
        return mTranslationRepository;
    }
}
