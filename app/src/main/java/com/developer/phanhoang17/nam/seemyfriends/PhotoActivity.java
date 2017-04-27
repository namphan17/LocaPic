package com.developer.phanhoang17.nam.seemyfriends;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Ngoc Nguyen account on 4/20/2017.
 */

public class PhotoActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, PhotoActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return PhotoFragment.newInstance();
    }
}
