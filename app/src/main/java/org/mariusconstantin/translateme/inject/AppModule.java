package org.mariusconstantin.translateme.inject;

import android.content.Context;

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
public class AppModule {

    @AppContext
    private final Context mAppContext;

    public AppModule(Context appContext) {
        mAppContext = appContext;
    }

    @Singleton
    @Provides
    public AppUtils provideAppUtils() {
        return new AppUtils();
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
        return new SharedPrefsRepo(appContext);
    }

}
