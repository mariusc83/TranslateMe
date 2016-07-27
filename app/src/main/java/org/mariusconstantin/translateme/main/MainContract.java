package org.mariusconstantin.translateme.main;

import org.mariusconstantin.translateme.IBasePresenter;
import org.mariusconstantin.translateme.IBaseView;

/**
 * Created by MConstantin on 7/26/2016.
 */
public interface MainContract {

    interface IMainPresenter extends IBasePresenter<IMainView> {

    }

    interface IMainView extends IBaseView<IMainPresenter> {

    }
}
