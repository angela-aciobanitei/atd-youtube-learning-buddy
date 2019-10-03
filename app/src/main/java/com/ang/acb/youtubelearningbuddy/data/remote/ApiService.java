package com.ang.acb.youtubelearningbuddy.data.remote;

import androidx.lifecycle.LiveData;

import com.ang.acb.youtubelearningbuddy.data.model.CommentListResponse;
import com.ang.acb.youtubelearningbuddy.data.model.SearchVideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Defines the REST API access points for Retrofit.
 *
 * See: https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 *
 */
public interface ApiService {

    // TODO See: https://developers.google.com/youtube/v3/docs/comments/list#parameters
    @GET("comments")
    Call<CommentListResponse> getComments(@Query("part") String part,
                                          @Query("pageToken") String pageToken);

    // TODO See: https://developers.google.com/youtube/v3/docs/search/list#parameters
    @GET("search")
    LiveData<ApiResponse<SearchVideosResponse>> searchVideos(@Query("part") String part,
                                                             @Query("q") String query);

    @GET("search")
    Call<SearchVideosResponse> searchVideos(@Query("part") String part,
                                            @Query("q") String query,
                                            @Query("pageToken") String pageToken);
}
