package com.developer.phanhoang17.nam.seemyfriends;

import android.support.v4.app.Fragment;

/**
 * Created by Ngoc Nguyen account on 2/23/2017.
 */

public class FriendListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FriendListFragment();
    }
}
