package org.mariusconstantin.translateme.main.inject;

import org.mariusconstantin.translateme.inject.PerFragment;
import org.mariusconstantin.translateme.main.translate.TranslateContract;
import org.mariusconstantin.translateme.main.translate.data.PersistedViewData;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;
import org.mariusconstantin.translateme.repositories.misc.LocalesRepository;
import org.mariusconstantin.translateme.repositories.misc.SharedPrefsRepo;

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

    @Provides
    @PerFragment
    public ILocalesRepository provideLocalesRepository() {
        return LocalesRepository.newInstance();
    }

    @Provides
    public PersistedViewData providePersistedViewData(SharedPrefsRepo sharedPrefsRepo,
                                                      ILocalesRepository localesRepository) {
        return new PersistedViewData(sharedPrefsRepo, localesRepository);
    }
}
