package org.mariusconstantin.translateme.main.translate.data;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.mariusconstantin.translateme.repositories.misc.CountryModel;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;
import org.mariusconstantin.translateme.repositories.misc.SharedPrefsRepo;

import java.util.Collections;
import java.util.List;

/**
 * Created by MConstantin on 8/10/2016.
 */
public class PersistedViewData {

    private static final String SELECTED_FROM_INDEX_KEY = "selected_from_index_key";
    private static final String SELECTED_TO_INDEX_KEY = "selected_to_index_key";
    private static final String PREV_INPUT_VALUE_KEY = "prev_input_key";

    @NonNull
    private final SharedPrefsRepo mSharedPrefsRepo;

    @NonNull
    private final ILocalesRepository mLocalesRepository;


    public PersistedViewData(@NonNull SharedPrefsRepo sharedPrefsRepo,
                             @NonNull ILocalesRepository localesRepository) {
        mSharedPrefsRepo = sharedPrefsRepo;
        mLocalesRepository = localesRepository;
    }

    public Bundle persistFromLanguageIndex(int index, @NonNull Bundle bundle) {
        bundle.putInt(SELECTED_FROM_INDEX_KEY, index);
        return bundle;
    }

    public Bundle persistToLanguageIndex(int index, @NonNull Bundle bundle) {
        bundle.putInt(SELECTED_TO_INDEX_KEY, index);
        return bundle;
    }

    public Bundle persistInputText(@NonNull String text, @NonNull Bundle bundle) {
        bundle.putString(PREV_INPUT_VALUE_KEY, text);
        return bundle;
    }

    public int getFromLanguageSpinnerIndexDefault(@Nullable Bundle savedState) {
        if (savedState != null && savedState.containsKey(SELECTED_FROM_INDEX_KEY))
            return savedState.getInt(SELECTED_FROM_INDEX_KEY);

        final String preferredCountry = mSharedPrefsRepo.getFromLanguageSelectedCountryValue();
        return getCountryIndex(preferredCountry);

    }

    public int getToLanguageSpinnerIndexDefault(@Nullable Bundle savedState) {
        if (savedState != null && savedState.containsKey(SELECTED_TO_INDEX_KEY))
            return savedState.getInt(SELECTED_TO_INDEX_KEY);

        final String preferredCountry = mSharedPrefsRepo.getToLanguageSelectedCountryValue();
        return getCountryIndex(preferredCountry);

    }

    private int getCountryIndex(@NonNull String preferredCountry) {
        if (!TextUtils.isEmpty(preferredCountry)) {
            final CountryModel countryModel = new CountryModel(preferredCountry, "");
            final List<CountryModel> countryModels = mLocalesRepository.getSupportedLanguages();
            final int index = Collections.binarySearch(countryModels, countryModel);
            if (index > 0) return index;
        }
        return 0;
    }

    public CharSequence getInputTextDefault(@Nullable Bundle savedState) {
        return savedState != null ? savedState.getString(PREV_INPUT_VALUE_KEY, "") : "";
    }

}
