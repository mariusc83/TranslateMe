package org.mariusconstantin.translateme.inject;

import android.content.Context;

import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.AppUtils;
import org.mariusconstantin.translateme.utils.ILogger;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MConstantin on 7/4/2016.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    AppUtils getAppUtils();

    @AppContext
    Context getAppContext();

    SharedPrefsRepo getSharedPrefsRepo();

    ILogger getLogger();
}
