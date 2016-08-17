package org.mariusconstantin.translateme.settings.inject;

import org.mariusconstantin.translateme.inject.PerFragment;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;
import org.mariusconstantin.translateme.repositories.misc.LocalesRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 8/12/2016.
 */
@Module
public class SettingsModule {
    @Provides
    @PerFragment
    public ILocalesRepository provideLocalesRepository() {
        return LocalesRepository.newInstance();
    }

}
