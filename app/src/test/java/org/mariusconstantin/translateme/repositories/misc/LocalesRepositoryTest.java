package org.mariusconstantin.translateme.repositories.misc;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.translateme.BuildConfig;
import org.mariusconstantin.translateme.repositories.misc.cache.LocalesCacheRepository;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by MConstantin on 7/29/2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LocalesRepositoryTest {

    ILocalesRepository mLocalesRepository;

    @Test
    public void testGetSupportedLanguages() {
        mLocalesRepository = LocalesRepository.newInstance();
        final List<CountryModel> countries = mLocalesRepository.getSupportedLanguages();
        assertThat(countries).isNotNull();
        CountryModel prevOne = null;
        for (CountryModel model
                : countries) {
            assertThat(model.getCountryCode()).isNotEmpty();
            assertThat(model.toString()).isNotEmpty();
            assertThat(model).isNotEqualTo(prevOne);
            prevOne = model;
        }
    }


    @Test
    public void testCacheData() {
        // given
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        final ILocalesRepository localRepo = mock(ILocalesRepository.class);
        final List<CountryModel> data = new ArrayList<>();
        given(localRepo.getSupportedLanguages()).willReturn(data);

        final ILocalesRepository cacheRepo = new LocalesCacheRepository();

        mLocalesRepository = LocalesRepository
                .newInstance(cacheRepo, localRepo);

        final List<CountryModel>[] dataHolder = new List[2];

        final Thread thread2 = new Thread(() -> {
            {
                dataHolder[1] = mLocalesRepository.getSupportedLanguages();
                countDownLatch.countDown();
            }
        });

        final Thread thread1 = new Thread(() -> {
            {
                dataHolder[0] = mLocalesRepository.getSupportedLanguages();
                thread2.start();
                countDownLatch.countDown();
            }
        });


        thread1.start();
        try {
            assertThat(countDownLatch.await(3, TimeUnit.SECONDS)).isTrue();
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

        verify(localRepo).getSupportedLanguages();
        verifyNoMoreInteractions(localRepo);
        assertThat(dataHolder[0]).isEqualTo(data);
        assertThat(dataHolder[1]).isEqualTo(data);
        cacheRepo.clearCache();
    }

    @Test
    public void testClearCacheData() {
        // given
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        final ILocalesRepository localRepo = mock(ILocalesRepository.class);
        final List<CountryModel> data = new ArrayList<>();
        given(localRepo.getSupportedLanguages()).willReturn(data);

        final ILocalesRepository cacheRepo = new LocalesCacheRepository();

        mLocalesRepository = LocalesRepository
                .newInstance(cacheRepo, localRepo);

        final List<CountryModel>[] dataHolder = new List[2];

        final Thread thread2 = new Thread(() -> {
            {
                dataHolder[1] = mLocalesRepository.getSupportedLanguages();
                countDownLatch.countDown();
            }
        });

        final Thread thread1 = new Thread(() -> {
            {
                dataHolder[0] = mLocalesRepository.getSupportedLanguages();
                mLocalesRepository.clearCache();
                thread2.start();
                countDownLatch.countDown();
            }
        });


        thread1.start();
        try {
            assertThat(countDownLatch.await(2, TimeUnit.SECONDS)).isTrue();
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

        verify(localRepo, times(2)).getSupportedLanguages();
        verifyNoMoreInteractions(localRepo);
        assertThat(dataHolder[0]).isEqualTo(data);
        assertThat(dataHolder[1]).isEqualTo(data);
        cacheRepo.clearCache();
    }

    @Test
    public void testSupportedCountries() throws Exception {
        mLocalesRepository = LocalesRepository.newInstance();
        final List<CharSequence> countries = mLocalesRepository.getSupportedCountries();
        assertThat(countries).isNotNull();
        CharSequence prevOne = null;
        for (CharSequence country
                : countries) {
            assertThat(country).isNotEmpty();
            assertThat(country).isNotEqualTo(prevOne);
            prevOne = country;
        }
    }

    @After
    public void tearDown() {
        mLocalesRepository.clearCache();
    }


}