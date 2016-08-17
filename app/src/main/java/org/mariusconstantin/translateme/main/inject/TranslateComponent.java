package org.mariusconstantin.translateme.main.inject;

import org.mariusconstantin.translateme.inject.PerFragment;
import org.mariusconstantin.translateme.main.translate.TranslatePresenter;
import org.mariusconstantin.translateme.main.translate.data.PersistedViewData;
import org.mariusconstantin.translateme.repositories.misc.ILocalesRepository;

import dagger.Component;

/**
 * Created by MConstantin on 7/26/2016.
 */
@Component(dependencies = MainComponent.class, modules = TranslateModule.class)
@PerFragment
public interface TranslateComponent {

    @PerFragment
    TranslatePresenter getTranslatePresenter();

    ILocalesRepository getLocalesRepository();

    PersistedViewData getPersistedViewData();
}
