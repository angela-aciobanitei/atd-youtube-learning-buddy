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
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 *
 */
public interface ApiService {

    // TODO See: https://developers.google.com/youtube/v3/docs/search/list#parameters
    // Ex: GET https://www.googleapis.com/youtube/v3/search?part=snippet&order=rating&q=android&type=video&key=[YOUR_API_KEY] HTTP/1.1
    @GET("search?part=id%2Csnippet&order=rating&type=video&maxResults=50")
    LiveData<ApiResponse<SearchVideosResponse>> searchVideos(@Query("q") String query);

    @GET("search?part=snippet&type=video&order=rating")
    Call<SearchVideosResponse> searchVideos2(@Query("q") String query,
                                            @Query("pageToken") String pageToken);

    // TODO See: https://developers.google.com/youtube/v3/docs/videos/list
    @GET("videos")
    Call<VideoListResponse> getVideo(@Query("id") String videoId);

    // TODO See: https://developers.google.com/youtube/v3/docs/commentThreads/list
    @GET("commentThreads?part=snippet%2Creplies")
    Call<CommentThreadListResponse> getComments(@Query("videoId") String videoId);
}
