package org.mariusconstantin.translateme.repositories.misc.local;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.mariusconstantin.translateme.repositories.misc.CountryModel;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;

import java.util.List;
import java.util.Locale;

/**
 * Created by MConstantin on 8/3/2016.
 */
public class LocalesLocalRepository implements ILocalesRepository {


    @NonNull
    @Override
    public List<CountryModel> getSupportedLanguages() {
        Locale[] locales = Locale.getAvailableLocales();
        return getSortedLocalesStream(locales)
                .collect(Collectors.toList());
    }

    private Stream<CountryModel> getSortedLocalesStream(Locale[] locales) {
        return Stream.of(locales)
                .filter(locale -> !TextUtils.isEmpty(locale.getDisplayLanguage()))
                .map(value -> new CountryModel(value.getDisplayLanguage(), value.getLanguage()))
                .distinct()
                .sorted();
    }

    @NonNull
    public List<CharSequence> getSupportedCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        final Stream<CharSequence> stream = getSortedLocalesStream(locales)
                .map(Object::toString);
        return stream.collect(Collectors.toList());
    }


    @Override
    public void clearCache() {
    }
}
