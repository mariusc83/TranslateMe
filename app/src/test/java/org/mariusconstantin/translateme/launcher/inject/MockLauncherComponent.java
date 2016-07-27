package org.mariusconstantin.translateme.launcher.inject;

import org.mariusconstantin.translateme.inject.MockAppComponent;
import org.mariusconstantin.translateme.inject.PerActivity;

import dagger.Component;

/**
 * Created by MConstantin on 7/4/2016.
 */
@PerActivity
@Component(dependencies = MockAppComponent.class, modules = MockLauncherModule.class)
public interface MockLauncherComponent extends LauncherComponent {
}
