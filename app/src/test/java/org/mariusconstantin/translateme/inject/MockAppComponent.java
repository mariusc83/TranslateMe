package org.mariusconstantin.translateme.inject;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MConstantin on 7/4/2016.
 */
@Singleton
@Component(modules = MockAppModule.class)
public interface MockAppComponent extends AppComponent {
}
