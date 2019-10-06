package com.ang.acb.youtubelearningbuddy.data.remote;

import androidx.lifecycle.LiveData;

import com.ang.acb.youtubelearningbuddy.data.model.CommentThreadListResponse;
import com.ang.acb.youtubelearningbuddy.data.model.SearchVideosResponse;
import com.ang.acb.youtubelearningbuddy.data.model.VideoListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Defines the REST API access points for Retrofit.
 *
 * See: https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
 * See: https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit
 */
public interface ApiService {

    // See: https://developers.google.com/youtube/v3/docs/search/list#parameters
    @GET("search?part=id%2Csnippet&type=video&maxResults=50")
    LiveData<ApiResponse<SearchVideosResponse>> searchVideos(@Query("q") String query);

    // See: https://developers.google.com/youtube/v3/docs/commentThreads/list#parameters
    // See: https://developers.google.com/youtube/v3/guides/implementation/comments
    @GET("commentThreads?part=snippet")
    LiveData<ApiResponse<CommentThreadListResponse>> getComments(@Query("videoId") String videoId);
}
