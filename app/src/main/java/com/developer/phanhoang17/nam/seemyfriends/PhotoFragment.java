package com.developer.phanhoang17.nam.seemyfriends;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by Ngoc Nguyen account on 4/20/2017.
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment_Nam";
    private RecyclerView mPhotoRecyclerView;
    private List<Photo> mItems = new ArrayList<>();

    private Profile mProfile;
    private AccessToken mAccessToken;

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }


    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mIdTextView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mIdTextView = (TextView) itemView;
        }

        public void bindPhotoItem(Photo item) {
            mIdTextView.setText(item.toString());
        }
    }
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<Photo> mPhotoItems;

        public PhotoAdapter(List<Photo> photoItems) {
            mPhotoItems = photoItems;
        }
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            Photo photoItem = mPhotoItems.get(position);
            photoHolder.bindPhotoItem(photoItem);
        }
        @Override
        public int getItemCount() {
            return mPhotoItems.size();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mAccessToken = getCurrentAccessToken();
        Toast.makeText(getContext(), mAccessToken + "NamHuong", Toast.LENGTH_LONG).show();
        new FetchPhotoTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);

        mPhotoRecyclerView = (RecyclerView) v
                .findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    private class FetchPhotoTask extends AsyncTask<Void, Void, List<Photo>> {
        @Override
        protected List<Photo> doInBackground(Void... params) {

            Log.i(TAG, "Executing in backgroud!!!");
            return new PhotoFetch().makeGraphRequest(mAccessToken);
        }

        @Override
        protected void onPostExecute(List<Photo> items) {
            mItems = items;
            Log.i(TAG, "Setting up Adapter!!!");
            setupAdapter();
        }
    }
}
