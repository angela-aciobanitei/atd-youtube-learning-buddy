package com.ang.acb.youtubelearningbuddy.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.ang.acb.youtubelearningbuddy.data.local.db.AppDatabase;
import com.ang.acb.youtubelearningbuddy.data.local.entity.CommentEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.SearchEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.model.CommentThreadListResponse;
import com.ang.acb.youtubelearningbuddy.data.model.Resource;
import com.ang.acb.youtubelearningbuddy.data.model.SearchResult;
import com.ang.acb.youtubelearningbuddy.data.model.SearchVideosResponse;
import com.ang.acb.youtubelearningbuddy.data.remote.ApiResponse;
import com.ang.acb.youtubelearningbuddy.data.remote.ApiService;
import com.ang.acb.youtubelearningbuddy.utils.AbsentLiveData;
import com.ang.acb.youtubelearningbuddy.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Repository module for handling data operations.
 *
 * See: https://developer.android.com/jetpack/docs/guide#truth
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 */
@Singleton
public class VideosRepository {

    private AppDatabase database;
    private ApiService apiService;
    private AppExecutors executors;

    @Inject
    VideosRepository(AppDatabase database, ApiService apiService, AppExecutors executors) {
        this.database = database;
        this.apiService = apiService;
        this.executors = executors;
    }

    public LiveData<Resource<List<VideoEntity>>> searchVideos(String query) {
        // Note that we are using the NetworkBoundResource<ResultType, RequestType> class
        // that we've created earlier which can provide a resource backed by both the
        // SQLite database and the network. It defines two type parameters, ResultType
        // and RequestType, because the data type used locally might not match the data
        // type returned from the API.
        return new NetworkBoundResource<List<VideoEntity>, SearchVideosResponse>(executors) {

            @NonNull
            @Override
            protected LiveData<ApiResponse<SearchVideosResponse>> createCall() {
                Timber.d("Create API call to YouTube search.list method.");
                return apiService.searchVideos(query);
            }

            @Override
            protected void saveCallResult(@NonNull SearchVideosResponse response) {
                // Save the YOU TUBE API response into the database.
                List<String> videoIds = response.getVideoIds();
                SearchEntity searchResult = new SearchEntity(query, videoIds,
                        response.getTotalResults(), response.getNextPageToken());

                database.runInTransaction(() -> {
                    long searchId = database.videoDao().insertSearchResult(searchResult);
                    Timber.d("Inserted search result with ID= %s in the db", searchId);
                    int savedVideosIds = database.videoDao().insertVideosFromResponse(response);
                    Timber.d("Inserted %s videos in the db", savedVideosIds);
                });
            }

            @NonNull
            @Override
            protected LiveData<List<VideoEntity>> loadFromDb() {
                Timber.d("Get the cached videos from the database");
                return Transformations.switchMap(database.videoDao().search(query), searchData -> {
                    if (searchData == null) return AbsentLiveData.create();
                    else return database.videoDao().loadVideosByIds(searchData.youTubeVideoIds);
                });
            }

            @Override
            protected boolean shouldFetch(@Nullable List<VideoEntity> dbData) {
                return dbData == null || dbData.isEmpty();
            }

            @NonNull
            @Override
            protected void onFetchFailed() {}
        }.asLiveData();
    }

    public LiveData<Resource<List<CommentEntity>>> getComments(String youTubeVideoId) {
        return new NetworkBoundResource<List<CommentEntity>, CommentThreadListResponse>(executors) {

            @NonNull
            @Override
            protected LiveData<ApiResponse<CommentThreadListResponse>> createCall() {
                Timber.d("Create API call to YouTube commentThreads.list method.");
                return apiService.getComments(youTubeVideoId);
            }

            @Override
            protected void saveCallResult(@NonNull CommentThreadListResponse response) {
                // Before trying to save the comments for a specific video in the db,
                // make sure that the video (which is the parent) actually exists in
                // the db, to avoid foreign key constraint fails.
                database.runInTransaction(() -> {
                    long roomVideoId = database.videoDao().getVideoId(youTubeVideoId);
                    int inserted = database.commentDao().insertCommentsFromResponse(roomVideoId, response);
                    Timber.d("Inserted %s comments in the db for video with Room ID= %s",
                            inserted, roomVideoId);

                });
            }

            @NonNull
            @Override
            protected LiveData<List<CommentEntity>> loadFromDb() {
                Timber.d("Get comments from db for video with YouTube ID= %s", youTubeVideoId);
                return database.commentDao().getCommentsForVideo(youTubeVideoId);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CommentEntity> dbData) {
                return dbData == null || dbData.isEmpty();
            }

            @NonNull
            @Override
            protected void onFetchFailed() {}
        }.asLiveData();
    }
}
