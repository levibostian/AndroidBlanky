package com.levibostian.androidblanky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.levibostian.androidblanky.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        startupFragment();
    }

    private void startupFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, MainFragment.newInstance())
                .commit();
    }

}
