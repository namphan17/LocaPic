package com.developer.phanhoang17.nam.seemyfriends;

import android.os.Bundle;
import android.util.Log;

import com.developer.phanhoang17.nam.seemyfriends.Models.Photo;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.GraphRequest.TAG;

/**
 * Created by Ngoc Nguyen account on 4/21/2017.
 */

public class PhotoFetch {
    final String[] afterString = {""};
    final boolean[] noData = {false};


    public List<Photo> makeGraphRequest(AccessToken accessToken) {
        final List<Photo> photos = new ArrayList<>();
        final AccessToken mAccessToken = accessToken;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final int[] count = {0};
                    count[0]++;
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "photos.limit(60){id, place, tags, likes.summary(true), images}");
                    // GraphRequest used to be final
                    final GraphRequest.Callback graphCallback = new GraphRequest.Callback() {
                    public void onCompleted(
                            GraphResponse response) {
                        // Application code
                        try {
                            Log.i("PhotoFetch.java", "Response: " + response.toString());
                            JSONObject jsonResponse = response.getJSONObject().getJSONObject("photos");
                            JSONArray jsonData = jsonResponse.getJSONArray("data");
                            parsePhotos(photos, jsonData, mAccessToken);

                                                if (jsonResponse.has("paging")) {
                                                    JSONObject paging = jsonResponse.getJSONObject("paging");
                                                    JSONObject cursors = paging.getJSONObject("cursors");
                                                    if(!cursors.isNull("after")){
                                                        Log.i("PhotoFetch.java", "After string = " + cursors.getString("after"));
                                                    }
//                                                    else {
//                                                        noData[0] = true;
//                                                    }
                                                }
//                                                else {
//                                                    noData[0] = true;
//                                                }
                                                Log.i("PhotoFetch.java", "Fetching the " + count[0] +
                                                        " page of photos; noData[0] = " + noData[0]);
                            GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.PREVIOUS);
                            Log.i("PhotoFetch.java", "Has next request: " + (nextRequest!=null));
                            if (nextRequest != null) {
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "photos{id, place, tags, likes.summary(true), images}");
                                nextRequest.setParameters(parameters);
                                nextRequest.setGraphPath("/me");
                                if (count[0] < 2 && nextRequest != null) {
                                    count[0]++;
                                    nextRequest.setCallback(this);
                                    nextRequest.executeAndWait();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new GraphRequest(mAccessToken,
                                    "/me",
                                    parameters,
                                    HttpMethod.GET,
                                    graphCallback).executeAndWait();
            }
        });
        t.start();
        try {
            t.join();
        }  catch(InterruptedException e) {
            e.printStackTrace();;
        }
        Log.i(TAG, "Photos fetched after the graph request: " + photos.size());
        return photos;
    }

    private void parsePhotos(List<Photo> photos,JSONArray jsonData, AccessToken accessToken)
            throws IOException, JSONException {
        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject photoJsonObject = jsonData.getJSONObject(i);
            Photo photoItem = new Photo(photoJsonObject.getString("id"));
            photoItem.setLikes(photoJsonObject.getJSONObject("likes")
                    .getJSONObject("summary")
                    .getInt("total_count"));

            if (photoJsonObject.has("tags")) {
                JSONArray tagData = photoJsonObject.getJSONObject("tags").getJSONArray("data");
                String[] tags = new String[tagData.length()];
                for (int j = 0; j < tagData.length(); j++) {
                    tags[j] = tagData.getJSONObject(j).getString("name");
                }
                photoItem.setTags(tags);
            }
            if (photoJsonObject.has("images")) {
                JSONArray imageUrls = photoJsonObject.getJSONArray("images");
                JSONObject imageData = (JSONObject) imageUrls.get(0);
                String imageUrl = imageData.getString("source");
                photoItem.setUrls(imageUrl);
            }
            photos.add(photoItem);
        }
    }
}
