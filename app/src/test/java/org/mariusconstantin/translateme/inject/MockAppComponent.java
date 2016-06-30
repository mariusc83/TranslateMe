package org.mariusconstantin.translateme.inject;

import android.content.Context;

import org.mariusconstantin.translateme.launcher.LauncherPresenter;
import org.mariusconstantin.translateme.launcher.inject.LauncherModule;
import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.AppUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MConstantin on 7/4/2016.
 */
@Singleton
@Component(modules = MockAppModule.class)
public interface MockAppComponent extends AppComponent {}
