package org.mariusconstantin.translateme.launcher.inject;

import android.content.Context;

import org.mariusconstantin.translateme.inject.AppComponent;
import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.inject.MockAppComponent;
import org.mariusconstantin.translateme.inject.PerActivity;
import org.mariusconstantin.translateme.launcher.LauncherPresenter;
import org.mariusconstantin.translateme.repositories.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.AppUtils;

import dagger.Component;

/**
 * Created by MConstantin on 7/4/2016.
 */
@PerActivity
@Component(dependencies = MockAppComponent.class, modules = MockLauncherModule.class)
public interface MockLauncherComponent extends LauncherComponent {}
