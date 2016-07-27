package org.mariusconstantin.translateme.main.inject;

import org.mariusconstantin.translateme.inject.AppComponent;
import org.mariusconstantin.translateme.inject.PerActivity;
import org.mariusconstantin.translateme.repositories.translation.TranslationRepository;
import org.mariusconstantin.translateme.utils.ILogger;

import dagger.Component;

/**
 * Created by MConstantin on 7/27/2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = MainModule.class)
public interface MainComponent {

    TranslationRepository getTranslationRepository();

    ILogger getLogger();
}
