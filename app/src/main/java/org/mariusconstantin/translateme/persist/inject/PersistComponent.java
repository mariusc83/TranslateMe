package org.mariusconstantin.translateme.persist.inject;

import org.mariusconstantin.translateme.inject.AppComponent;
import org.mariusconstantin.translateme.persist.DatabaseHelper;

import dagger.Component;

/**
 * Created by MConstantin on 8/23/2016.
 */
@Component(dependencies = AppComponent.class, modules = PersistModule.class)
@PerPersist
public interface PersistComponent {

    @PerPersist
    DatabaseHelper getDatabaseHelper();
}
