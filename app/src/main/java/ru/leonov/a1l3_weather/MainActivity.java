package ru.leonov.a1l3_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMessage(this.getClass().getName() + " - onCreate()");

        setContentView(R.layout.activity_main);
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
