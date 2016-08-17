package org.mariusconstantin.translateme.main.inject;

import org.mariusconstantin.translateme.main.translate.TranslateContract;
import org.mariusconstantin.translateme.main.translate.data.PersistedViewData;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;
import org.mariusconstantin.translateme.repositories.misc.LocalesRepository;
import org.mariusconstantin.translateme.repositories.misc.SharedPrefsRepo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 7/27/2016.
 */
@Module
public class MockTranslateModule {
    private final TranslateContract.ITranslateView mTranslateView;
    private final ILocalesRepository mLocalesRepository;
    private final SharedPrefsRepo mSharedPrefsRepo;

    public MockTranslateModule(TranslateContract.ITranslateView translateView,
                               ILocalesRepository localesRepository,
                               SharedPrefsRepo sharedPrefsRepo) {
        mTranslateView = translateView;
        mLocalesRepository = localesRepository;
        mSharedPrefsRepo = sharedPrefsRepo;
    }

    @Provides
    public TranslateContract.ITranslateView provideTranslateView() {
        return mTranslateView;
    }

    @Provides
    public ILocalesRepository provideLocalesRepository() {
        return LocalesRepository.newInstance();
    }

    @Provides
    public PersistedViewData providePersistedViewData(SharedPrefsRepo sharedPrefsRepo,
                                                      ILocalesRepository localesRepository) {
        return new PersistedViewData(sharedPrefsRepo, localesRepository);
    }
}
