package org.mariusconstantin.translateme.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.TranslateApplication;
import org.mariusconstantin.translateme.main.inject.DaggerMainComponent;
import org.mariusconstantin.translateme.main.inject.MainComponent;
import org.mariusconstantin.translateme.main.inject.MainModule;
import org.mariusconstantin.translateme.main.translate.TranslateFragment;
import org.mariusconstantin.translateme.settings.SettingsActivity;

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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) switchToTranslatePage();
    }

    private void switchToTranslatePage() {
        final TranslateFragment translateFragment = new TranslateFragment();
        final Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.root_container);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            ft.replace(R.id.root_container, translateFragment);
        } else {
            ft.add(R.id.root_container, translateFragment);
        }
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_prefs_id:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return false;
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
