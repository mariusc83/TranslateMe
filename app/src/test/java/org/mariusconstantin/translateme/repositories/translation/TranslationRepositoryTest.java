package org.mariusconstantin.translateme.repositories.translation;

import com.google.gson.JsonSyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.translateme.BuildConfig;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.times;

/**
 * Created by MConstantin on 7/26/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TranslationRepositoryTest {

    @Mock
    TranslationNetworkProvider mMockNetworkProvider;

    @Mock
    private TranslationModel mMockTranslationModel;

    private TranslationRepository mTranslationRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mTranslationRepository = new TranslationRepository(mMockNetworkProvider)
                .subscribingOn(AndroidSchedulers.mainThread());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_an_exception_was_thrown() throws Exception {
        // given
        final JsonSyntaxException exception = new JsonSyntaxException("test");
        given(mMockNetworkProvider.translate("test"))
                .willThrow(exception);

        // when
        final Observer<TranslationModel> mockObserver = mock(Observer.class);
        final Observable<TranslationModel> observable = mTranslationRepository.translate("test");
        observable.subscribe(mockObserver);

        // then
        assertThat(observable).isNotNull();
        verify(mockObserver, never()).onNext(eq(mMockTranslationModel));
        verify(mockObserver, never()).onCompleted();
        verify(mockObserver).onError(any(Throwable.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_successful_translation() throws Exception {
        // given
        given(mMockNetworkProvider.translate("test"))
                .willReturn(mMockTranslationModel);

        // when
        final Observer<TranslationModel> mockObserver = mock(Observer.class);
        final Observable<TranslationModel> observable = mTranslationRepository.translate("test");
        observable.subscribe(mockObserver);

        // then
        assertThat(observable).isNotNull();
        verify(mockObserver).onNext(eq(mMockTranslationModel));
        verify(mockObserver).onCompleted();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_subscribing_again_to_same_observable() throws Exception {
        // given
        given(mMockNetworkProvider.translate("test"))
                .willReturn(mMockTranslationModel);

        // when
        final Observer<TranslationModel> mockObserver = mock(Observer.class);
        final Observable<TranslationModel> observable = mTranslationRepository.translate("test");
        observable.subscribe(mockObserver);
        observable.subscribe(mockObserver);

        // then
        assertThat(observable).isNotNull();
        verify(mockObserver, times(2)).onNext(eq(mMockTranslationModel));
        verify(mockObserver, times(2)).onCompleted();
    }
}