package org.mariusconstantin.translateme.main.inject;

import org.mariusconstantin.translateme.inject.PerFragment;

import dagger.Component;

/**
 * Created by MConstantin on 7/27/2016.
 */
@Component(dependencies = MockMainComponent.class, modules = MockTranslateModule.class)
@PerFragment
public interface MockTranslateComponent extends TranslateComponent {

}
