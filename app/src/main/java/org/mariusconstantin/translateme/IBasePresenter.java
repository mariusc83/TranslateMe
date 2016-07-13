package org.mariusconstantin.translateme;

/**
 * Created by MConstantin on 7/1/2016.
 */
public interface IBasePresenter<D extends IBaseView> {

    void onStart();

    void onStop();
}
