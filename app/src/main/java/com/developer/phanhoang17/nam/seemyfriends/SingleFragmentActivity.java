package com.developer.phanhoang17.nam.seemyfriends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.facebook.FacebookSdk;

/**
 * Created by Ngoc Nguyen account on 2/23/2017.
 */

public abstract class SingleFragmentActivity extends android.support.v4.app.FragmentActivity {

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.profile_fragment_container, fragment)
                    .commit();
        }
    }
}
