package org.mariusconstantin.translateme.settings;

import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.IBasePresenter;
import org.mariusconstantin.translateme.IBaseView;

/**
 * Created by MConstantin on 8/8/2016.
 */
public interface SettingsContract {

    interface ISettingsView extends IBaseView<ISettingsPresenter> {

    }

    interface ISettingsPresenter extends IBasePresenter<ISettingsView> {

        @NonNull
        CharSequence[] getLocalCountries();
    }


}
