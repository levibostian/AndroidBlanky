package com.levibostian.androidblanky.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.levibostian.androidblanky.R;

// Extend this class to have an activity with back button in top left corner.
public abstract class BaseBackButtonActivity extends BaseActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_back_button);

        mToolbar = (Toolbar) findViewById(R.id.back_toolbar);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle(getScreenTitle());
    }

    protected void setFragmentInFragmentContainer(Fragment fragment) {
        getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, fragment)
                            .commit();
    }

    protected void replaceFragmentInFragmentContainer(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                                                          .beginTransaction()
                                                          .replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    protected abstract int getScreenTitle();

    private void setupViews() {
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
