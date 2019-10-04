package com.ang.acb.youtubelearningbuddy.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.ang.acb.youtubelearningbuddy.data.local.db.AppDatabase;
import com.ang.acb.youtubelearningbuddy.data.local.entity.SearchVideosResult;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.model.Resource;
import com.ang.acb.youtubelearningbuddy.data.model.SearchVideosResponse;
import com.ang.acb.youtubelearningbuddy.data.remote.ApiResponse;
import com.ang.acb.youtubelearningbuddy.data.remote.ApiService;
import com.ang.acb.youtubelearningbuddy.utils.AbsentLiveData;
import com.ang.acb.youtubelearningbuddy.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository module for handling data operations.
 *
 * See: https://developer.android.com/jetpack/docs/guide#truth
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 */
@Singleton
public class VideosRepository {

    private AppDatabase appDatabase;
    private ApiService apiService;
    private AppExecutors appExecutors;

    @Inject
    public VideosRepository(AppDatabase appDatabase, ApiService apiService, AppExecutors appExecutors) {
        this.appDatabase = appDatabase;
        this.apiService = apiService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<VideoEntity>>> searchVideos(String query) {
        // Note that we are using the NetworkBoundResource<ResultType, RequestType> class
        // that we've created earlier which can provide a resource backed by both the
        // SQLite database and the network. It defines two type parameters, ResultType
        // and RequestType, because the data type used locally might not match the data
        // type returned from the API.
        return new NetworkBoundResource<List<VideoEntity>, SearchVideosResponse>(appExecutors) {

            @NonNull
            @Override
            protected LiveData<ApiResponse<SearchVideosResponse>> createCall() {
                // Create the call to YOU TUBE API.
                return apiService.searchVideos(query);
            }

            @Override
            protected void saveCallResult(@NonNull SearchVideosResponse response) {
                // Save the YOU TUBE API response into the database.
                List<String> videoIds = response.getVideoIds();
                SearchVideosResult searchVideosResult = new SearchVideosResult(
                        query, videoIds, response.getTotalResults(), response.getNextPageToken());
                appDatabase.beginTransaction();
                try {
                    appDatabase.videoDao().insertSearchVideosResult(searchVideosResult);
                    appDatabase.videoDao().insertVideosFromResponse(response);
                    appDatabase.setTransactionSuccessful();
                } finally {
                    appDatabase.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<VideoEntity> dbData) {
                // Decide whether to fetch potentially updated data from the network.
                return dbData == null;
            }

            @NonNull
            @Override
            protected LiveData<List<VideoEntity>> loadFromDb() {
                // Get the cached data from the database.
                return Transformations.switchMap(appDatabase.videoDao().search(query), searchData -> {
                    if (searchData == null) return AbsentLiveData.create();
                    else return appDatabase.videoDao().loadVideosByIds(searchData.youTubeVideoIds);
                });
            }

            @NonNull
            @Override
            protected void onFetchFailed() {}
        }.asLiveData();
    }
}
