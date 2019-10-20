package ru.leonov.a1l3_weather;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import ru.leonov.a1l3_weather.Fragments.SelectCityFragment;
import ru.leonov.a1l3_weather.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TAG";
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMessage(this.getClass().getName() + " - onCreate()");
        setContentView(R.layout.activity_main);

        initToolBar();
        initSideMenu();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettingsActivity();
            return true;
        }
        if (id == R.id.action_about) {
            showAboutActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingsActivity() {
        Intent intent = new Intent();
        intent.setClass(Objects.requireNonNull(getBaseContext()), SettingsFragment.class);
        startActivity(intent);
    }

    private void showAboutActivity() {
        Intent intent = new Intent();
        intent.setClass(Objects.requireNonNull(getBaseContext()), AboutActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Создадим новый фрагмент
        Fragment fragment = null;
        Class fragmentClass = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_city) {
            fragmentClass = SelectCityFragment.class;
        } else if (id == R.id.nav_settings) {
            fragmentClass = SettingsFragment.class;
        } else if (id == R.id.nav_about) {

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), R.string.SomeWrong, Toast.LENGTH_LONG).show();
            return false;
        }

        // Вставляем фрагмент, заменяя текущий фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // Выделяем выбранный пункт меню в шторке
        //item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        //setTitle(item.getTitle());

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


    //region  [ для задания ]
    @Override
    protected void onStart() {
        super.onStart();
        showMessage(this.getClass().getName() + " - onStart()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        showMessage(this.getClass().getName() + " - onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMessage(this.getClass().getName() + " - onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showMessage(this.getClass().getName() + " - onPause()");
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        showMessage(this.getClass().getName() + " - onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showMessage(this.getClass().getName() + " - onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showMessage(this.getClass().getName() + " - onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showMessage(this.getClass().getName() + " - onDestroy()");
    }
    //endregion

    private void showMessage(String message) {
        Log.d(TAG, message);
    }
}
