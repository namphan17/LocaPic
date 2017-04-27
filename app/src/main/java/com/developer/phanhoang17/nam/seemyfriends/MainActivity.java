package com.developer.phanhoang17.nam.seemyfriends;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.facebook.Profile;

public class MainActivity extends SingleFragmentActivity {
    private static final String TAG = "ProfileActivity_Nam";

    public static Intent newIntent(Context packageContext, Profile profile) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra("firstName", profile.getFirstName());
        intent.putExtra("lastName", profile.getLastName());
        intent.putExtra("imageUrl", profile.getProfilePictureUri(200,200).toString());
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        Intent receivedIntent = getIntent();
        String firstName = receivedIntent.getStringExtra("firstName");
        String lastName = receivedIntent.getStringExtra("lastName");
        String imageUrl = receivedIntent.getStringExtra("imageUrl");

        return ProfileFragment.newInstance(firstName, lastName, imageUrl);
    }
}
