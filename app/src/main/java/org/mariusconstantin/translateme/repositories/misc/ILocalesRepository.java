package org.mariusconstantin.translateme.repositories.misc;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by MConstantin on 8/3/2016.
 */
public interface ILocalesRepository {
    List<CountryModel> getSupportedLanguages();

    @NonNull
    List<CharSequence> getSupportedCountries();

    void clearCache();
}
