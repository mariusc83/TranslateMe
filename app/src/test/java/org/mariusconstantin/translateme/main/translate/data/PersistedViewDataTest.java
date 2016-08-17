package org.mariusconstantin.translateme.main.translate.data;


import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.translateme.BuildConfig;
import org.mariusconstantin.translateme.repositories.misc.CountryModel;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;
import org.mariusconstantin.translateme.repositories.misc.SharedPrefsRepo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by MConstantin on 8/11/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class PersistedViewDataTest {

    @Mock
    ILocalesRepository mMockLocalesRepository;

    @Mock
    SharedPrefsRepo mMockSharedPrefsRepo;

    PersistedViewData mPersistedViewData;

    final CountryModel model1 = new CountryModel(Locale.US.getDisplayCountry(), Locale.US
            .getLanguage());
    final CountryModel model2 = new CountryModel(Locale.UK.getDisplayCountry(), Locale.UK
            .getLanguage());
    final CountryModel model3 = new CountryModel(Locale.FRANCE.getDisplayCountry(), Locale.FRANCE
            .getLanguage());

    final List<CountryModel> mMockLocales = Arrays.asList(model1, model2, model3);


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mPersistedViewData = new PersistedViewData(mMockSharedPrefsRepo,
                mMockLocalesRepository);
        given(mMockLocalesRepository.getSupportedLanguages()).willReturn(mMockLocales);

    }

    @Test
    public void when_no_from_language_persisted_in_prefs_or_bundle() {
        // given
        given(mMockSharedPrefsRepo.getFromLanguageSelectedCountryValue())
                .willReturn("");

        // when
        final int index = mPersistedViewData.getFromLanguageSpinnerIndexDefault(null);

        // then
        assertThat(index).isEqualTo(0);
    }

    @Test
    public void when_no_to_language_persisted_in_prefs_or_bundle() {
        // given
        given(mMockSharedPrefsRepo.getToLanguageSelectedCountryValue())
                .willReturn("");

        // when
        final int index = mPersistedViewData.getToLanguageSpinnerIndexDefault(null);

        // then
        assertThat(index).isEqualTo(0);
    }

    @Test
    public void when_from_language_persisted_in_bundle_but_not_matching_available_locales() {
        // given
        given(mMockSharedPrefsRepo.getFromLanguageSelectedCountryValue())
                .willReturn("XY");

        // when
        final int index = mPersistedViewData.getFromLanguageSpinnerIndexDefault(null);

        // then
        assertThat(index).isEqualTo(0);
    }

    @Test
    public void when_from_language_persisted_in_bundle() {
        // given
        final Bundle bundle = new Bundle();
        mPersistedViewData.persistFromLanguageIndex(1, bundle);
        given(mMockSharedPrefsRepo.getToLanguageSelectedCountryValue())
                .willReturn("");

        // when
        final int index = mPersistedViewData.getFromLanguageSpinnerIndexDefault(bundle);

        // then
        assertThat(index).isEqualTo(1);
    }

    @Test
    public void when_from_language_persisted_in_shared_prefs() {
        // given
        given(mMockSharedPrefsRepo.getFromLanguageSelectedCountryValue())
                .willReturn(Locale.UK.getDisplayCountry());

        // when
        final int index = mPersistedViewData.getFromLanguageSpinnerIndexDefault(null);

        // then
        assertThat(index).isEqualTo(1);
    }

}