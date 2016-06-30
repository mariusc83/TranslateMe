package org.mariusconstantin.translateme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.mariusconstantin.translateme.inject.AppComponent;

/**
 * Created by MConstantin on 7/4/2016.
 */
public class BaseActivity extends AppCompatActivity {

    @NonNull
    AppComponent getAppComponent() {
        return TranslateApplication.fromContext(this).getAppComponent();
    }
}
