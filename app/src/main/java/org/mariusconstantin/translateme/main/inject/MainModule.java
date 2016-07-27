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
public class MainModule {

    @Provides
    public TranslationNetworkProvider provideTranslationNetworkProvider() {
        return new TranslationNetworkProvider();
    }

    @PerActivity
    @Provides
    public TranslationRepository provideTranslationRepository(
            @NonNull TranslationNetworkProvider networkProvider) {
        return new TranslationRepository(networkProvider);
    }
}
