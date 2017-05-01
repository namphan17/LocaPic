package com.developer.phanhoang17.nam.seemyfriends;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.developer.phanhoang17.nam.seemyfriends.Models.Photo;
import com.facebook.AccessToken;
import com.facebook.Profile;

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
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    // Caching variables
//    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    private AccessToken mAccessToken;

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }


    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mPhotoItemView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mPhotoItemView = (ImageView) itemView
                    .findViewById(R.id.fragment_photo_image_view);
        }

        public void bindDrawable(Drawable drawable) {
            mPhotoItemView.setImageDrawable(drawable);
        }
    }
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<Photo> mPhotoItems;

        public PhotoAdapter(List<Photo> photoItems) {
            mPhotoItems = photoItems;
        }
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.photo_item, viewGroup, false);
            return new PhotoHolder(view);
        }
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            Photo photoItem = mPhotoItems.get(position);
            photoHolder.bindDrawable(getResources().getDrawable(R.drawable.em_yeu));
            mThumbnailDownloader.queueThumbnail(photoHolder, photoItem.getUrls());
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
        AccessToken[] params = new AccessToken[1];
        params[0] = mAccessToken;
        Toast.makeText(getContext(), mAccessToken + "NamHuong", Toast.LENGTH_LONG).show();
        Log.i(TAG, "Before running Async; AccessToken: " + mAccessToken);
        mItems = new PhotoFetch().makeGraphRequest(mAccessToken);
        Log.i(TAG, "Number of photos: " + mItems.size());

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder target, Bitmap thumbnail) {
                        Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                        target.bindDrawable(drawable);
                    }
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread has started!");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed!");
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }
}
