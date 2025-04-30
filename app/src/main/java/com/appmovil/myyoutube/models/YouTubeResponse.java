package com.appmovil.myyoutube.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class YouTubeResponse {

    @SerializedName("items")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    @SerializedName("nextPageToken")
    private String nextPageToken;

    public String getNextPageToken() {
        return nextPageToken;
    }

}
