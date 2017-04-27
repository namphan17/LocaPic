package com.developer.phanhoang17.nam.seemyfriends;

import android.os.Bundle;
import android.util.Log;

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

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by Ngoc Nguyen account on 4/21/2017.
 */

public class PhotoFetch {
    final String[] afterString = {""};
    final boolean[] noData = {false};

    public List<Photo> makeGraphRequest(AccessToken accessToken) {
        final List<Photo> photos = new ArrayList<>();

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, place, tags, likes, images");
        GraphRequest request = new GraphRequest(
                accessToken,
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
                            Log.i("PhotoFetch.java", "DataLength:" + jsonData.length());
                            parsePhotos(photos, jsonData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        request.executeAsync();
        return photos;
    }

    private void parsePhotos(List<Photo> photos,JSONArray jsonData)
            throws IOException, JSONException {
        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject photoJsonObject = jsonData.getJSONObject(i);
            Photo photoItem = new Photo(photoJsonObject.getString("id"));
            if (photoJsonObject.has("tags")) {
                JSONArray tagData = photoJsonObject.getJSONObject("tags").getJSONArray("data");
                String[] tags = new String[tagData.length()];
                for (int j = 0; j < tagData.length(); j++) {
                    tags[j] = tagData.getJSONObject(j).getString("name");
                }
                photoItem.setTags(tags);
            }
            photos.add(photoItem);
        }
    }
}
