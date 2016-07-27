package org.mariusconstantin.translateme.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.TranslateApplication;
import org.mariusconstantin.translateme.main.inject.DaggerMainComponent;
import org.mariusconstantin.translateme.main.inject.MainComponent;
import org.mariusconstantin.translateme.main.inject.MainModule;
import org.mariusconstantin.translateme.main.translate.TranslateFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @NonNull
    private MainComponent mMainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainComponent = DaggerMainComponent
                .builder()
                .appComponent(TranslateApplication.fromContext(this).getAppComponent())
                .mainModule(new MainModule())
                .build();
        setContentView(R.layout.activity_main);
        switchToTranslatePage();
    }

    private void switchToTranslatePage() {
        final TranslateFragment translateFragment = new TranslateFragment();
        final Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.root_container);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            ft.replace(R.id.root_container, translateFragment);
        } else {
            ft.add(R.id.root_container, null);
        }
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    public MainComponent getMainComponent() {
        return mMainComponent;
    }
}
