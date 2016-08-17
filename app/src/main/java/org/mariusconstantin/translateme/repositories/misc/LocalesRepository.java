package org.mariusconstantin.translateme.repositories.misc;

import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.repositories.misc.cache.LocalesCacheRepository;
import org.mariusconstantin.translateme.repositories.misc.local.LocalesLocalRepository;

import java.util.List;

/**
 * Created by MConstantin on 7/28/2016.
 */
public class LocalesRepository implements ILocalesRepository {

    @NonNull
    private final ILocalesRepository mLocalesCacheRepository;

    @NonNull
    private final ILocalesRepository mLocalesLocalRepository;

    public static ILocalesRepository newInstance(
            @NonNull ILocalesRepository localesCacheRepository,
            @NonNull ILocalesRepository localesLocalRepository) {
        return new LocalesRepository(localesCacheRepository, localesLocalRepository);
    }

    public static ILocalesRepository newInstance() {
        return new LocalesRepository(getDefaultCacheRepository(), getDefaultLocalRepository());
    }

    private static ILocalesRepository getDefaultLocalRepository() {
        return new LocalesLocalRepository();
    }

    private static LocalesCacheRepository getDefaultCacheRepository() {
        return new LocalesCacheRepository();
    }


    private LocalesRepository(@NonNull ILocalesRepository localesCacheRepository,
                              @NonNull ILocalesRepository localesLocalRepository) {
        mLocalesCacheRepository = localesCacheRepository;
        mLocalesLocalRepository = localesLocalRepository;
    }

    @Override
    public void clearCache() {
        mLocalesCacheRepository.clearCache();
    }

    @Override
    @NonNull
    public List<CountryModel> getSupportedLanguages() {
        final List<CountryModel> cache = mLocalesCacheRepository.getSupportedLanguages();
        if (cache != null) {
            return cache;
        }
        final List<CountryModel> data = mLocalesLocalRepository.getSupportedLanguages();
        ((LocalesCacheRepository) mLocalesCacheRepository).saveData(data);
        return data;
    }

    @NonNull
    @Override
    public List<CharSequence> getSupportedCountries() {
        return mLocalesLocalRepository.getSupportedCountries();
    }
}
