package ru.leonov.a1l3_weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMessage(this.getClass().getName() + " - onCreate()");
        setContentView(R.layout.activity_main);

        initToolBar();

    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        intent.setClass(Objects.requireNonNull(getBaseContext()), SettingsActivity.class);
        startActivity(intent);
    }

    private void showAboutActivity() {
        Intent intent = new Intent();
        intent.setClass(Objects.requireNonNull(getBaseContext()), AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int countOfFragmentInManager = getSupportFragmentManager().getBackStackEntryCount();
        if(countOfFragmentInManager > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    // для задания

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

    private void showMessage(String message) {
        Log.d(TAG, message);
    }


}
