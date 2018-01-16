package com.example.ayush.androidchallenge.AndroidLifecycle;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.ayush.androidchallenge.R;

/***
 * We are using fragments as different screens and controlling them with MainActivity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showSearchFragment();
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void showSearchFragment(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame, new SearchFragment())
                .commit();
    }
}
