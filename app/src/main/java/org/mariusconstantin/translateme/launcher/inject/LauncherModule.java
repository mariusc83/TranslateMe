package org.mariusconstantin.translateme.launcher.inject;

import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.PerActivity;
import org.mariusconstantin.translateme.launcher.LauncherContract;
import org.mariusconstantin.translateme.repositories.GoogleTokenRepo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MConstantin on 7/4/2016.
 */
@Module
public class LauncherModule {
    @NonNull
    private final LauncherContract.ILauncherView mView;

    public LauncherModule(@NonNull LauncherContract.ILauncherView view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public LauncherContract.ILauncherView provideView() {
        return mView;
    }


    @PerActivity
    @Provides
    public GoogleTokenRepo provideGoogleTokenRepo() {
        return new GoogleTokenRepo();
    }
}
