package org.mariusconstantin.translateme.main.translate;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.translateme.BuildConfig;
import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.inject.DaggerMockAppComponent;
import org.mariusconstantin.translateme.inject.MockAppComponent;
import org.mariusconstantin.translateme.inject.MockAppModule;
import org.mariusconstantin.translateme.main.inject.DaggerMockMainComponent;
import org.mariusconstantin.translateme.main.inject.DaggerMockTranslateComponent;
import org.mariusconstantin.translateme.main.inject.MockMainComponent;
import org.mariusconstantin.translateme.main.inject.MockMainModule;
import org.mariusconstantin.translateme.main.inject.MockTranslateComponent;
import org.mariusconstantin.translateme.main.inject.MockTranslateModule;
import org.mariusconstantin.translateme.repositories.GoogleTokenRepo;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;
import org.mariusconstantin.translateme.repositories.misc.SharedPrefsRepo;
import org.mariusconstantin.translateme.repositories.translation.TranslatedTextModel;
import org.mariusconstantin.translateme.repositories.translation.TranslationDataModel;
import org.mariusconstantin.translateme.repositories.translation.TranslationModel;
import org.mariusconstantin.translateme.repositories.translation.TranslationNetworkProvider;
import org.mariusconstantin.translateme.repositories.translation.TranslationRepository;
import org.mariusconstantin.translateme.utils.AppUtils;
import org.mariusconstantin.translateme.utils.ILogger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.when;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.BDDMockito.verifyZeroInteractions;
import static org.mockito.BDDMockito.anyString;

/**
 * Created by MConstantin on 7/27/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TranslatePresenterTest {

    private static final String TRANSLATED_TEXT_VALUE = "Translated text";

    @Mock
    AppUtils mMockAppUtils;

    @AppContext
    @Mock
    Context mMockAppContext;

    @Mock
    GoogleTokenRepo mMockGoogleTokenRepo;

    @Mock
    ILogger mMockLogger;

    @Mock
    TranslationNetworkProvider mMockNetworkProvider;

    @Mock
    TranslationRepository mMockTranslationRepository;

    @Mock
    TranslateContract.ITranslateView mMockTranslateView;

    @Mock
    TranslationModel mMockTranslationModel;

    @Mock
    TranslationDataModel mMockTranslationDataModel;

    @Mock
    TranslatedTextModel mMockTranslatedTextModel;

    @Mock
    List<TranslatedTextModel> mMockTranslatedTextData;

    @Mock
    Throwable mMockThrowable;

    @Mock
    ILocalesRepository mMockLocalesRepository;

    @Mock
    SharedPrefsRepo mMockSharedPrefsRepo;



    private TranslatePresenter mPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mMockTranslationModel.getData()).thenReturn(mMockTranslationDataModel);
        when(mMockTranslationDataModel.getTranslations()).thenReturn(mMockTranslatedTextData);
        when(mMockTranslatedTextData.size()).thenReturn(1);
        when(mMockTranslatedTextData.isEmpty()).thenReturn(false);
        when(mMockTranslatedTextData.get(0)).thenReturn(mMockTranslatedTextModel);
        when(mMockTranslatedTextModel.translatedText()).thenReturn(TRANSLATED_TEXT_VALUE);
        MockAppComponent mockAppComponent = DaggerMockAppComponent
                .builder()
                .mockAppModule(new MockAppModule(mMockAppContext,
                        mMockAppUtils,
                        mMockSharedPrefsRepo,
                        mMockLogger))
                .build();
        MockMainComponent mockMainComponent = DaggerMockMainComponent
                .builder()
                .mockAppComponent(mockAppComponent)
                .mockMainModule(new MockMainModule(mMockNetworkProvider,
                        mMockTranslationRepository))
                .build();

        MockTranslateComponent translateComponent = DaggerMockTranslateComponent.builder()
                .mockMainComponent(mockMainComponent)
                .mockTranslateModule(new MockTranslateModule(mMockTranslateView,
                        mMockLocalesRepository,
                        mMockSharedPrefsRepo))
                .build();

        mPresenter = translateComponent.getTranslatePresenter();
        assertThat(mPresenter).isNotNull();
        verify(mMockTranslateView).setPresenter(mPresenter);
    }

    @Test
    public void when_translation_requested_and_succeeded() throws Exception {
        // given
        final Observable<TranslationModel> mockObservable = Observable.create(subscriber -> {
            subscriber.onNext(mMockTranslationModel);
            subscriber.onCompleted();
        });
        given(mMockTranslateView.isActive()).willReturn(true);
        given(mMockTranslationRepository.translate(anyString())).willReturn(mockObservable);

        // when
        mPresenter.translate("test");

        // then
        verify(mMockTranslateView).isActive();
        verify(mMockTranslateView).displayMessage(eq(TRANSLATED_TEXT_VALUE));
        verifyNoMoreInteractions(mMockTranslateView);
    }

    @Test
    public void when_translation_requested_and_but_the_view_is_inactive() throws Exception {
        // given
        final Observable<TranslationModel> mockObservable = Observable.create(subscriber -> {
            subscriber.onNext(mMockTranslationModel);
            subscriber.onCompleted();
        });
        given(mMockTranslateView.isActive()).willReturn(false);
        given(mMockTranslationRepository.translate(anyString())).willReturn(mockObservable);

        // when
        mPresenter.translate("test");

        // then
        verify(mMockTranslateView).isActive();
        verify(mMockTranslateView, times(0)).displayMessage(eq(TRANSLATED_TEXT_VALUE));
        verifyNoMoreInteractions(mMockTranslateView);
    }

    @Test
    public void when_translation_requested_and_not_succeeded() throws Exception {
        // given
        final Observable<TranslationModel> mockObservable = Observable.create(subscriber -> {
            subscriber.onError(mMockThrowable);
            subscriber.onCompleted();
        });
        given(mMockTranslateView.isActive()).willReturn(true);
        given(mMockTranslationRepository.translate(anyString())).willReturn(mockObservable);

        // when
        mPresenter.translate("test");

        // then
        verify(mMockTranslateView).isActive();
        verify(mMockTranslateView)
                .displayMessage(eq(R.string.error_occured_while_translating_message));
        verify(mMockLogger).e(anyString(), eq(mMockThrowable), anyString());
        verifyNoMoreInteractions(mMockTranslateView);
    }

    @Test
    public void when_translation_requested_but_the_presenter_was_stopped() throws Exception {
        // given
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Observable<TranslationModel> mockObservable =
                Observable.<TranslationModel>create(subscriber -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    } finally {
                        countDownLatch.countDown();
                    }
                    subscriber.onNext(mMockTranslationModel);
                    subscriber.onCompleted();
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        given(mMockTranslateView.isActive()).willReturn(true);
        given(mMockTranslationRepository.translate(anyString())).willReturn(mockObservable);

        // when
        mPresenter.translate("test");
        mPresenter.onStop();

        try {
            countDownLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
        // then
        verifyZeroInteractions(mMockTranslateView);
    }

    @Test
    public void when_translation_requested_but_the_presenter_was_stopped_and_resumed()
            throws Exception {
        // given
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        final Observable<TranslationModel> mockObservable =
                Observable.<TranslationModel>create(subscriber -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    subscriber.onNext(mMockTranslationModel);
                    subscriber.onCompleted();
                    countDownLatch.countDown();
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        given(mMockTranslateView.isActive()).willReturn(true);
        given(mMockTranslationRepository.translate(anyString())).willReturn(mockObservable);

        // when
        mPresenter.translate("test");
        mPresenter.onStop();

        // then
        verifyZeroInteractions(mMockTranslateView);


        // when
        mPresenter.onStart();
        try {
            countDownLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

        // then
        Robolectric.flushForegroundThreadScheduler();
        verify(mMockTranslateView).isActive();
        verify(mMockTranslateView).displayMessage(eq(TRANSLATED_TEXT_VALUE));
        verifyNoMoreInteractions(mMockTranslateView);
    }
}