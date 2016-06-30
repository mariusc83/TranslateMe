package org.mariusconstantin.translateme;

import android.support.annotation.NonNull;

/**
 * Created by MConstantin on 7/4/2016.
 */
public interface IBaseChildView<D extends IBasePresenter> extends IBaseView<D> {
    void setPresenter(@NonNull D presenter);

}
