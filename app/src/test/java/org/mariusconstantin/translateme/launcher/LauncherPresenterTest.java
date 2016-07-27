package org.mariusconstantin.translateme.launcher;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.translateme.BuildConfig;
import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.inject.DaggerMockAppComponent;
import org.mariusconstantin.translateme.inject.MockAppComponent;
import org.mariusconstantin.translateme.inject.MockAppModule;
import org.mariusconstantin.translateme.launcher.inject.DaggerMockLauncherComponent;
import org.mariusconstantin.translateme.launcher.inject.MockLauncherComponent;
import org.mariusconstantin.translateme.launcher.inject.MockLauncherModule;
import org.mariusconstantin.translateme.repositories.GoogleTokenRepo;
import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;
import org.mariusconstantin.translateme.repositories.translation.TranslationNetworkProvider;
import org.mariusconstantin.translateme.repositories.translation.TranslationRepository;
import org.mariusconstantin.translateme.utils.AppUtils;
import org.mariusconstantin.translateme.utils.ILogger;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.never;

/**
 * Created by MConstantin on 7/4/2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 23, constants = BuildConfig.class)
public class LauncherPresenterTest {

    @Mock
    LauncherContract.ILauncherView mMockView;

    @Mock
    AppUtils mMockAppUtils;

    @AppContext
    @Mock
    Context mMockAppContext;

    @Mock
    SharedPrefsRepo mMockSharedPrefsRepo;

    @Mock
    GoogleTokenRepo mMockGoogleTokenRepo;

    @Mock
    ILogger mMockLogger;

    @Mock
    TranslationNetworkProvider mMockNetworkProvider;

    @Mock
    TranslationRepository mMockTranslationRepository;

    LauncherPresenter mLauncherPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockAppComponent mockAppComponent = DaggerMockAppComponent
                .builder()
                .mockAppModule(new MockAppModule(mMockAppContext,
                        mMockAppUtils,
                        mMockSharedPrefsRepo,
                        mMockLogger))
                .build();
        MockLauncherComponent mockLauncherComponent = DaggerMockLauncherComponent
                .builder()
                .mockAppComponent(mockAppComponent)
                .mockLauncherModule(new MockLauncherModule(mMockView, mMockGoogleTokenRepo))
                .build();
        mLauncherPresenter = mockLauncherComponent.getLauncherPresenter();
        assertThat(mLauncherPresenter).isNotNull();
    }


    @Test
    public void when_start_without_permissions() {
        // given
        given(mMockView.checkPermissions()).willReturn(false);

        // when
        mLauncherPresenter.onStart();

        // then
        InOrder inOrder = inOrder(mMockView);
        inOrder.verify(mMockView).checkPermissions();
        verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void when_start_with_permissions_no_play_services_available() {
        // given
        given(mMockView.checkPermissions()).willReturn(true);
        given(mMockView.checkGooglePlayServices()).willReturn(false);

        // when
        mLauncherPresenter.onStart();

        // then
        InOrder inOrder = inOrder(mMockView);
        inOrder.verify(mMockView).checkPermissions();
        inOrder.verify(mMockView).checkGooglePlayServices();
        verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void when_start_with_permissions_and_play_services_available() {
        // given
        given(mMockView.checkPermissions()).willReturn(true);
        given(mMockView.checkGooglePlayServices()).willReturn(true);
        given(mMockSharedPrefsRepo.getStoredSelectedGoogleAccount())
                .willReturn(SharedPrefsRepo.NO_ACCOUNT);

        // when
        mLauncherPresenter.onStart();

        // then
        InOrder inOrder = inOrder(mMockView, mMockSharedPrefsRepo);
        inOrder.verify(mMockView).checkPermissions();
        inOrder.verify(mMockView).checkGooglePlayServices();
        inOrder.verify(mMockSharedPrefsRepo).getStoredSelectedGoogleAccount();
        inOrder.verify(mMockView).pickAccounts();
        verifyNoMoreInteractions(mMockView);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void when_start_with_permissions_play_services_and_saved_google_account_available() {
        // given
        final String testAccountName = "test@gmail.com";
        final String testToken = "test";
        final Observable<String> mockObservable = Observable.create(subscriber -> {
            subscriber.onNext(testToken);
            subscriber.onCompleted();
        });

        given(mMockView.checkPermissions()).willReturn(true);
        given(mMockView.checkGooglePlayServices()).willReturn(true);
        given(mMockSharedPrefsRepo.getStoredSelectedGoogleAccount()).willReturn(testAccountName);
        given(mMockGoogleTokenRepo.getToken(anyString(),
                anyString(),
                any(Context.class))).willReturn(mockObservable);
        // when
        mLauncherPresenter.onStart();

        // then
        InOrder inOrder = inOrder(mMockView, mMockSharedPrefsRepo, mMockGoogleTokenRepo);
        inOrder.verify(mMockView).checkPermissions();
        inOrder.verify(mMockView).checkGooglePlayServices();
        inOrder.verify(mMockSharedPrefsRepo).getStoredSelectedGoogleAccount();
        inOrder.verify(mMockGoogleTokenRepo).getToken(eq(testAccountName),
                eq(LauncherContract.Scopes.GOOGLE_TRANSLATE_API_SCOPE), any(Context.class));
        verify(mMockView).goToNextView(eq(testToken));
    }

    @Test
    public void when_going_from_resume_to_pause_and_back_must_subscribe_to_same_observable() {
        // given
        final String testAccountName = "test@gmail.com";
        final String testToken = "test";
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        final Observable mockObservable = Observable.create(subscriber -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            subscriber.onNext(testToken);
            subscriber.onCompleted();
            countDownLatch.countDown();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        given(mMockView.checkPermissions()).willReturn(true);
        given(mMockView.checkGooglePlayServices()).willReturn(true);
        given(mMockSharedPrefsRepo.getStoredSelectedGoogleAccount()).willReturn(testAccountName);
        given(mMockGoogleTokenRepo.getToken(
                anyString(),
                anyString(),
                any(Context.class)))
                .willReturn(mockObservable);
        // when
        mLauncherPresenter.onStart();
        mLauncherPresenter.onStop();

        // then
        InOrder inOrder = inOrder(mMockView, mMockSharedPrefsRepo, mMockGoogleTokenRepo);
        inOrder.verify(mMockView).checkPermissions();
        inOrder.verify(mMockView).checkGooglePlayServices();
        inOrder.verify(mMockSharedPrefsRepo).getStoredSelectedGoogleAccount();
        inOrder.verify(mMockGoogleTokenRepo).getToken(eq(testAccountName),
                eq(LauncherContract.Scopes.GOOGLE_TRANSLATE_API_SCOPE), any(Context.class));
        inOrder.verify(mMockView, never()).goToNextView(eq(testToken));

        // when
        mLauncherPresenter.onStart();

        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }

        Robolectric.flushForegroundThreadScheduler();
        verifyNoMoreInteractions(mMockSharedPrefsRepo);
        verifyNoMoreInteractions(mMockGoogleTokenRepo);
        verify(mMockView).goToNextView(eq(testToken));
    }


}