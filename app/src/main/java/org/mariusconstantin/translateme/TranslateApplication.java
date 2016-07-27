package org.mariusconstantin.translateme;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import org.mariusconstantin.translateme.inject.AppComponent;
import org.mariusconstantin.translateme.inject.AppModule;
import org.mariusconstantin.translateme.inject.DaggerAppComponent;

/**
 * Created by MConstantin on 7/4/2016.
 */
public class TranslateApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this.getApplicationContext()))
                .build();

    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static TranslateApplication fromContext(@NonNull Context context) {
        return (TranslateApplication) context.getApplicationContext();
    }

}
