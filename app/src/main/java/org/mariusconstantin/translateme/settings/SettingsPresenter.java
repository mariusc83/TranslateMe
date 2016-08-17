package org.mariusconstantin.translateme.settings;

import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.PerFragment;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by MConstantin on 8/12/2016.
 */
public class SettingsPresenter implements SettingsContract.ISettingsPresenter {
    @NonNull
    private final ILocalesRepository mLocalesRepository;

    @PerFragment
    @Inject
    public SettingsPresenter(@NonNull ILocalesRepository localesRepository) {
        mLocalesRepository = localesRepository;
    }


    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @NonNull
    @Override
    public CharSequence[] getLocalCountries() {
        final List<CharSequence> countries = mLocalesRepository.getSupportedCountries();
        final CharSequence[] countriesArray = new CharSequence[countries.size()];
        return countries.toArray(countriesArray);
    }
}
