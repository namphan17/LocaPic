package com.developer.phanhoang17.nam.seemyfriends;

/**
 * Created by Ngoc Nguyen account on 4/21/2017.
 */

public class Photo {
    private final String mId;
    private String[] mTags;
    private int mLikes;

    public String[] getTags() {
        return mTags;
    }

    public void setTags(String[] mTags) {
        this.mTags = mTags;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int mLikes) {
        this.mLikes = mLikes;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public String[] getUrls() {
        return mUrls;
    }

    public void setUrls(String[] mUrls) {
        this.mUrls = mUrls;
    }

    private String mPlace;
    private String[] mUrls;

    public Photo(String id) {
        mId = id;
    }


    @Override
    public String toString() {
        return mId;
    }
}
