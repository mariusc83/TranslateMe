package org.mariusconstantin.translateme.repositories.misc.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mariusconstantin.translateme.repositories.misc.CountryModel;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by MConstantin on 8/3/2016.
 */
public class LocalesCacheRepository implements ILocalesRepository {
    private static AtomicReference<List<CountryModel>> sCachedList = new AtomicReference<>();


    public void saveData(@NonNull List<CountryModel> data) {
        sCachedList.compareAndSet(null, data);
    }

    @Override
    public void clearCache() {
        final List<CountryModel> data = sCachedList.get();
        sCachedList.compareAndSet(data, null);
    }

    @NonNull
    @Override
    public List<CharSequence> getSupportedCountries() {
        return Collections.EMPTY_LIST;
    }

    @Nullable
    @Override
    public List<CountryModel> getSupportedLanguages() {
        return sCachedList.get();
    }
}
