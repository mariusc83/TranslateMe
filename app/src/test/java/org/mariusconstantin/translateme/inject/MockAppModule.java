package org.mariusconstantin.translateme.inject;

import android.content.Context;
import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.AppUtils;
import org.mariusconstantin.translateme.utils.ILogger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 7/4/2016.
 */
@Module
public class MockAppModule {

    @AppContext
    private final Context mAppContext;

    @NonNull
    private final AppUtils mAppUtils;

    @NonNull
    private final SharedPrefsRepo mSharedPrefsRepo;

    @NonNull
    private final ILogger mLogger;

    public MockAppModule(@NonNull Context appContext,
                         @NonNull AppUtils appUtils,
                         @NonNull SharedPrefsRepo sharedPrefsRepo,
                         @NonNull ILogger logger) {
        mAppContext = appContext;
        mAppUtils = appUtils;
        mSharedPrefsRepo = sharedPrefsRepo;
        mLogger = logger;
    }

    @Singleton
    @Provides
    public AppUtils provideAppUtils() {
        return mAppUtils;
    }

    @AppContext
    @Singleton
    @Provides
    public Context provideAppContext() {
        return mAppContext;
    }

    @Singleton
    @Provides
    public SharedPrefsRepo provideSharedPrefsRepo(@AppContext Context appContext) {
        return mSharedPrefsRepo;
    }

    @Provides
    public ILogger provideLogger() {
        return mLogger;
    }
}
