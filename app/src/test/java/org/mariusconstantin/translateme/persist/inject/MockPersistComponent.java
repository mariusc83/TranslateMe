package org.mariusconstantin.translateme.persist.inject;

import org.mariusconstantin.translateme.inject.AppComponent;

import dagger.Component;

/**
 * Created by MConstantin on 9/6/2016.
 */
@Component(dependencies = AppComponent.class, modules = MockPersistModule.class)
@PerPersist
public interface MockPersistComponent extends PersistComponent {
}
