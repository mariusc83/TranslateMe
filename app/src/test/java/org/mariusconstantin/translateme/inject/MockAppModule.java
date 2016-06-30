package org.mariusconstantin.translateme.inject;

import android.content.Context;
import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.AppUtils;

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

    public MockAppModule(Context appContext, @NonNull AppUtils appUtils, @NonNull SharedPrefsRepo sharedPrefsRepo) {
        mAppContext = appContext;
        mAppUtils = appUtils;
        mSharedPrefsRepo = sharedPrefsRepo;
    }

    @Singleton
    @Provides
    public AppUtils provideAppUtils(){
        return mAppUtils;
    }

    @AppContext
    @Singleton
    @Provides
    public Context provideAppContext(){
        return mAppContext;
    }

    @Singleton
    @Provides
    public SharedPrefsRepo provideSharedPrefsRepo(@AppContext Context appContext) {
        return mSharedPrefsRepo;
    }
}
