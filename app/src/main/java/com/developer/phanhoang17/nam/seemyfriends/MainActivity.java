package com.developer.phanhoang17.nam.seemyfriends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;

import java.io.InputStream;

import static com.facebook.AccessToken.getCurrentAccessToken;
import static com.facebook.Profile.getCurrentProfile;

public class MainActivity extends AppCompatActivity {

    private ShareDialog shareDialog;
    private TextView mNameTextView;

    private Button mLogoutButton;
    private Button mFriendListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Share diaglog
        shareDialog = new ShareDialog(this);

        Profile profile = getCurrentProfile();
        final AccessToken accessToken = getCurrentAccessToken();

        // Getting user information.
        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();

        // Display the name
        mNameTextView = (TextView)findViewById(R.id.nameAndSurname);
        mNameTextView.setText("" + name + " " + surname);
        // Display the profile picture
        new DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);

        mFriendListButton = (Button)findViewById(R.id.friendlist_button);
        mFriendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent friendListIntent = new Intent(MainActivity.this, FriendListActivity.class);
//                startActivity(friendListIntent);
                Toast.makeText(MainActivity.this, accessToken.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mLogoutButton = (Button)findViewById(R.id.logout);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
