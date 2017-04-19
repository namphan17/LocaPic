package com.developer.phanhoang17.nam.seemyfriends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import static com.facebook.AccessToken.getCurrentAccessToken;
import static com.facebook.Profile.getCurrentProfile;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_Friend";

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

        final Profile profile = getCurrentProfile();
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
                Toast.makeText(MainActivity.this, accessToken.getToken()+"NamHuong", Toast.LENGTH_LONG).show();
                new FetchInfoTask().execute(profile);
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

    public class FetchInfoTask extends AsyncTask<Profile, Void, Void> {
        @Override
        protected Void doInBackground(Profile... profile) {

            Log.i(TAG, "Executing in backgroud!!!");

            Profile myProfile = profile[0];
            final String[] afterString = {""};
            final boolean[] noData = {false};

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, place, tags");
            GraphRequest request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/photos",
                    parameters,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(
                                GraphResponse response) {
                            // Application code
//                            System.out.println("HERE: " + response.getJSONObject().toString());
                            try {
                                JSONObject jsonResponse = response.getJSONObject();
                                JSONArray jsonData = jsonResponse.getJSONArray("data");
                                System.out.println("Data Length: " + jsonData.length());
                                JSONObject item = (JSONObject) jsonData.get(24);
                                System.out.println("Last item: " + item.toString());
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });


            request.executeAsync();
//            do {
//                Bundle param = new Bundle();
//                param.putString("fields", "photos");
////                param.putString("next", afterString[0]);
//
////                System.out.println("Checking No Data before request: " + noData[0]);
//
//                /* make the API call */
//                new GraphRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        myProfile.getId(),
//                        param,
//                        HttpMethod.GET,
//                        new GraphRequest.Callback() {
//                            public void onCompleted(GraphResponse response) {
//                                /* handle the result */
//
//                                try {
//                                    System.out.println(response.getJSONObject().toString());
//                                    JSONObject jsonObject = response.getJSONObject().getJSONObject("photos");
//                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                                    System.out.println("JSON data length: " + jsonArray.length());
////                                    System.out.println(response.toString());
//
//                                    if (!jsonObject.isNull("paging")) {
//                                        JSONObject paging = jsonObject.getJSONObject("paging");
//                                        JSONObject cursors = paging.getJSONObject("cursors");
//
//                                        System.out.println("Has next: " + !cursors.isNull("next"));
//
//                                        if (!cursors.isNull("next"))
//                                            afterString[0] = cursors.getString("next");
//                                        else
//                                            noData[0] = true;
//                                    } else {
//                                        noData[0] = true;
//                                    }
//
//                                    System.out.println("No data: " + noData[0]);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                ).executeAndWait();
//            } while (!noData[0] == true);
            return null;
        }
    }

}
