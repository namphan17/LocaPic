package com.developer.phanhoang17.nam.seemyfriends;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
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
import java.util.UUID;

import static com.facebook.AccessToken.getCurrentAccessToken;
import static com.facebook.Profile.getCurrentProfile;

/**
 * Created by Ngoc Nguyen account on 2/23/2017.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment_Nam";

    private ShareDialog shareDialog;
    private TextView mNameTextView;

    private Button mLogoutButton;
    private Button mPhotoButton;


    public static ProfileFragment newInstance(String firstName, String lastName, String picUrl) {
        Bundle args = new Bundle();
        args.putSerializable("firstName", firstName);
        args.putSerializable("lastName", lastName);
        args.putSerializable("picUrl", picUrl);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Share diaglog
        shareDialog = new ShareDialog(this);

        final Profile profile = getCurrentProfile();
        final AccessToken accessToken = getCurrentAccessToken();

        // Getting user information.
        Bundle inBundle = getArguments();
        String name = inBundle.getString("firstName");
        String surname = inBundle.getString("lastName");
        String imageUrl = inBundle.getString("picUrl");

        // Display the name
        mNameTextView = (TextView) view.findViewById(R.id.nameAndSurname);
        mNameTextView.setText("" + name + " " + surname);
        // Display the profile picture
        new DownloadImage((ImageView) view.findViewById(R.id.profileImage)).execute(imageUrl);

        mPhotoButton = (Button) view.findViewById(R.id.photo_button);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PhotoActivity.newIntent(getContext());
                startActivity(intent);
            }
        });

        mLogoutButton = (Button) view.findViewById(R.id.logout);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(getContext(), LoginActivity.class);
                startActivity(login);
                getActivity().finish();
            }
        });

        return view;
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
