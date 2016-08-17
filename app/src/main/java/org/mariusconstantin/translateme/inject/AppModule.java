package org.mariusconstantin.translateme.inject;

import android.content.Context;

import org.mariusconstantin.translateme.BuildConfig;
import org.mariusconstantin.translateme.repositories.misc.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.AppUtils;
import org.mariusconstantin.translateme.utils.DebugLogger;
import org.mariusconstantin.translateme.utils.DefaultLogger;
import org.mariusconstantin.translateme.utils.ILogger;

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

    @Provides
    public ILogger provideLogger() {
        return BuildConfig.DEBUG ? new DebugLogger() : new DefaultLogger();
    }
}
