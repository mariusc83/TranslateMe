package org.mariusconstantin.translateme.main.inject;

import org.mariusconstantin.translateme.inject.MockAppComponent;
import org.mariusconstantin.translateme.inject.PerActivity;

import dagger.Component;

/**
 * Created by MConstantin on 7/27/2016.
 */
@PerActivity
@Component(dependencies = MockAppComponent.class, modules = MockMainModule.class)
public interface MockMainComponent extends MainComponent {

}
