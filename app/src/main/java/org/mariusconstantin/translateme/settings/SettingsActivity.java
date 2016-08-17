package org.mariusconstantin.translateme.settings;

import android.preference.PreferenceActivity;

import org.mariusconstantin.translateme.R;

import java.util.List;

/**
 * Created by MConstantin on 8/8/2016.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }
}
