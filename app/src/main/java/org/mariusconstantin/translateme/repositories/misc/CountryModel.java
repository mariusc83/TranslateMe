package org.mariusconstantin.translateme.repositories.misc;

import android.support.annotation.NonNull;

public class CountryModel {
    @NonNull
    private final String mCountry;
    @NonNull
    private final String mCountryCode;

    public CountryModel(@NonNull String country, @NonNull String countryCode) {
        mCountry = country;
        mCountryCode = countryCode;
    }

    @NonNull
    public String getCountryCode() {
        return mCountryCode;
    }

    @Override
    public String toString() {
        return mCountry;
    }
}
