package org.mariusconstantin.translateme.main.translate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import org.mariusconstantin.translateme.IBaseChildView;
import org.mariusconstantin.translateme.IBasePresenter;
import org.mariusconstantin.translateme.repositories.misc.CountryModel;

import java.util.List;

/**
 * Created by MConstantin on 7/26/2016.
 */
public interface TranslateContract {

    interface ITranslateView extends IBaseChildView<ITranslatePresenter> {

        void displayMessage(@NonNull String s);

        void displayMessage(@StringRes int messageId);

        void setFromLanguageSpinnerSelection(int selection);

        void setToLanguageSpinnerSelection(int selection);
    }

    interface ITranslatePresenter extends IBasePresenter<ITranslateView> {

        List<CountryModel> getAvailableLanguages();

        void translate(String toTranslate, String fromLang, String toLang);

        void trimMemory(int level);

        void persistInputText(String s, @NonNull Bundle outState);

        void persistToLanguageSpinnerPosition(int selectedItemPosition, @NonNull Bundle outState);

        void persistFromLanguageSpinnerPosition(int selectedItemPosition, @NonNull Bundle outState);


        int getFromLanguageSpinnerIndexDefault(@Nullable Bundle savedState);

        int getToLanguageSpinnerIndexDefault(@Nullable Bundle savedState);

        CharSequence getInputTextDefault(@Nullable Bundle savedState);
    }
}
