package org.mariusconstantin.translateme.launcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mariusconstantin.translateme.IBasePresenter;
import org.mariusconstantin.translateme.IBaseView;

/**
 * Created by MConstantin on 7/4/2016.
 */
public interface LauncherContract {
    interface Scopes{
        String GOOGLE_TRANSLATE_API_SCOPE = "oauth2:https://www.googleapis.com/auth/translate";
    }

    interface ILauncherView extends IBaseView<ILauncherPresenter> {

        boolean checkPermissions();

        boolean checkGooglePlayServices();

        void pickAccounts();

        void handleRequestTokenError(Throwable e);

        void goToNextView(@NonNull String token);
    }

    interface ILauncherPresenter extends IBasePresenter<ILauncherView> {

        void checkAccountAndRequestToken();

        void saveAccount(@NonNull String account);

        void requestToken(@NonNull String accountName);

        void saveToken(@NonNull String token);

        void resetPickAccountsRequested();

        void resetTokenWasRequested();
    }
}
