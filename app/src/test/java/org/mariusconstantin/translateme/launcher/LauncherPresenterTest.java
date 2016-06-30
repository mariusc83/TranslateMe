package org.mariusconstantin.translateme.launcher;

import android.content.Context;

import org.junit.After;
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
import org.mariusconstantin.translateme.utils.AppUtils;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.inOrder;
import static org.mockito.BDDMockito.*;

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

    LauncherPresenter mLauncherPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockAppComponent mockAppComponent = DaggerMockAppComponent
                .builder()
                .mockAppModule(new MockAppModule(mMockAppContext, mMockAppUtils, mMockSharedPrefsRepo))
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
    public void test_when_start_without_permissions() {
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
    public void test_when_start_with_permissions_no_play_services_available() {
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
    public void test_when_start_with_permissions_and_play_services_available() {
        // given
        given(mMockView.checkPermissions()).willReturn(true);
        given(mMockView.checkGooglePlayServices()).willReturn(true);
        given(mMockSharedPrefsRepo.getStoredSelectedGoogleAccount()).willReturn(SharedPrefsRepo.NO_ACCOUNT);

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
    public void test_when_start_with_permissions_play_services_and_saved_google_account_available() {
        // given
        final String testAccountName = "test@gmail.com";
        final Observable<String> mockObservable = Observable.create(subscriber -> {
            subscriber.onNext("test");
            subscriber.onCompleted();
        });

        given(mMockView.checkPermissions()).willReturn(true);
        given(mMockView.checkGooglePlayServices()).willReturn(true);
        given(mMockSharedPrefsRepo.getStoredSelectedGoogleAccount()).willReturn(testAccountName);
        given(mMockGoogleTokenRepo.getToken(anyString(), anyString(), any(Context.class))).willReturn(mockObservable);
        // when
        mLauncherPresenter.onStart();

        // then
        InOrder inOrder = inOrder(mMockView, mMockSharedPrefsRepo, mMockGoogleTokenRepo);
        inOrder.verify(mMockView).checkPermissions();
        inOrder.verify(mMockView).checkGooglePlayServices();
        inOrder.verify(mMockSharedPrefsRepo).getStoredSelectedGoogleAccount();
        inOrder.verify(mMockGoogleTokenRepo).getToken(eq(testAccountName), eq(LauncherContract.Scopes.GOOGLE_TRANSLATE_API_SCOPE), any(Context.class));
        verifyNoMoreInteractions(mMockView);
    }


}