package org.mariusconstantin.translateme.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.view.View;

import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.settings.inject.DaggerSettingsComponent;
import org.mariusconstantin.translateme.settings.inject.SettingsComponent;
import org.mariusconstantin.translateme.settings.inject.SettingsModule;

/**
 * Created by MConstantin on 8/8/2016.
 */
public class SettingsFragment extends PreferenceFragment implements SettingsContract.ISettingsView {

    private SettingsContract.ISettingsPresenter mPresenter;
    private SettingsComponent mSettingsComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsComponent = DaggerSettingsComponent
                .builder()
                .settingsModule(new SettingsModule())
                .build();
        mPresenter = mSettingsComponent.getSettingsPresenter();
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListPreference fromListPreference = (ListPreference) getPreferenceScreen()
                .findPreference(getString(R.string.preferred_language_from_key));
        final ListPreference toListPreference = (ListPreference) getPreferenceScreen()
                .findPreference(getString(R.string.preferred_language_to_key));

        final CharSequence[] localCountries = mPresenter.getLocalCountries();
        fromListPreference.setEntries(localCountries);
        fromListPreference.setEntryValues(localCountries);
        toListPreference.setEntries(localCountries);
        toListPreference.setEntryValues(localCountries);
    }

    @Override
    public boolean isActive() {
        return isVisible();
    }
}
