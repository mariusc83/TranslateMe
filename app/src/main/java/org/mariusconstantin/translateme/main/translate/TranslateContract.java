package org.mariusconstantin.translateme.main.translate;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import org.mariusconstantin.translateme.IBaseChildView;
import org.mariusconstantin.translateme.IBasePresenter;

/**
 * Created by MConstantin on 7/26/2016.
 */
public interface TranslateContract {

    interface ITranslateView extends IBaseChildView<ITranslatePresenter> {

        void displayMessage(@NonNull String s);

        void displayMessage(@StringRes int messageId);
    }

    interface ITranslatePresenter extends IBasePresenter<ITranslateView> {

        void translate(@NonNull String toTranslate);
    }
}
