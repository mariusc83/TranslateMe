package org.mariusconstantin.translateme.repositories.misc;

import android.support.annotation.NonNull;
import android.text.TextUtils;

public class CountryModel implements Comparable<CountryModel> {
    @NonNull
    private final String mCountry;
    @NonNull
    private final String mCountryCode;

    public CountryModel(@NonNull String country, @NonNull String countryCode) {
        mCountry = country;
        mCountryCode = countryCode.toLowerCase();
    }

    @NonNull
    public String getCountryCode() {
        return mCountryCode;
    }

    @Override
    public String toString() {
        return mCountry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountryModel that = (CountryModel) o;

        return TextUtils.equals(mCountry, that.mCountry)
                && TextUtils.equals(mCountryCode, that.mCountryCode);
    }

    @Override
    public int hashCode() {
        int result = mCountry.hashCode();
        result = 31 * result + mCountryCode.hashCode();
        return result;
    }

    @Override
    public int compareTo(@NonNull CountryModel another) {
        return mCountry.compareTo(another.mCountry);
    }
}
