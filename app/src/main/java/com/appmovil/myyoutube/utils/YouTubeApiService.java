package com.appmovil.myyoutube.utils;

import com.appmovil.myyoutube.models.YouTubeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeApiService {
    @GET("search")
    Call<YouTubeResponse> searchVideos(
            @Query("part") String part,
            @Query("q") String query,
            @Query("key") String apiKey,
            @Query("maxResults") int maxResults,
            @Query("type") String type,
            @Query("pageToken") String pageToken
    );
}
