package ru.leonov.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import ru.leonov.weather.fragments.AboutFragment;
import ru.leonov.weather.fragments.FeedBackFragment;
import ru.leonov.weather.fragments.SelectCityFragment;
import ru.leonov.weather.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String BACK_STACK_KEY = "backStackKey";
    private static final String TAG = "WEATHER";

    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMessage(this.getClass().getName() + " - onCreate()");
        setContentView(R.layout.activity_main);

        initToolBar();
        initSideMenu();
        showSelectCityFragment();
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initSideMenu() {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showSelectCityFragment() {
        Fragment fragment;
        try {
            fragment = Objects.requireNonNull(SelectCityFragment.class).newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        } catch (IllegalAccessException | InstantiationException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Fragment fragment;
        Class fragmentClass = null;

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            fragmentClass = SettingsFragment.class;
        }
        else if (id == R.id.action_about) {
            fragmentClass = AboutFragment.class;
        }

        try {
            fragment = (Fragment) Objects.requireNonNull(fragmentClass).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), R.string.SomeWrong, Toast.LENGTH_LONG).show();
            return false;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(BACK_STACK_KEY)
                .commit();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment;
        Class fragmentClass = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_city) {
            fragmentClass = SelectCityFragment.class;
        } else if (id == R.id.nav_settings) {
            fragmentClass = SettingsFragment.class;
        } else if (id == R.id.nav_feedback) {
            fragmentClass = FeedBackFragment.class;
        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
        }

        try {
            fragment = (Fragment) Objects.requireNonNull(fragmentClass).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), R.string.SomeWrong, Toast.LENGTH_LONG).show();
            return false;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(BACK_STACK_KEY)
                .commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            int countOfFragmentInManager = getSupportFragmentManager().getBackStackEntryCount();
            if(countOfFragmentInManager > 0) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void showMessage(String message) {
        Log.d(TAG, message);
    }
}