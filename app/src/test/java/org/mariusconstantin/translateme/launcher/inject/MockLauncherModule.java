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
public class MockLauncherModule {
    @NonNull
    private final LauncherContract.ILauncherView mView;

    @NonNull
    private final GoogleTokenRepo mGoogleTokenRepo;

    public MockLauncherModule(@NonNull LauncherContract.ILauncherView view,
                              @NonNull GoogleTokenRepo googleTokenRepo) {
        mView = view;
        mGoogleTokenRepo = googleTokenRepo;
    }

    @PerActivity
    @Provides
    public LauncherContract.ILauncherView provideView() {
        return mView;
    }

    @PerActivity
    @Provides
    public GoogleTokenRepo provideGoogleTokenRepo() {
        return mGoogleTokenRepo;
    }

}
