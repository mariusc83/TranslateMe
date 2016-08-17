package org.mariusconstantin.translateme.settings.inject;

import org.mariusconstantin.translateme.inject.PerFragment;
import org.mariusconstantin.translateme.settings.SettingsPresenter;

import dagger.Component;

/**
 * Created by MConstantin on 8/12/2016.
 */
@Component(modules = SettingsModule.class)
@PerFragment
public interface SettingsComponent {

    @PerFragment
    SettingsPresenter getSettingsPresenter();
}
