package com.ang.acb.youtubelearningbuddy.data.repository;

import androidx.lifecycle.LiveData;

import com.ang.acb.youtubelearningbuddy.data.local.db.AppDatabase;
import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoTopicJoin;
import com.ang.acb.youtubelearningbuddy.data.remote.ApiService;
import com.ang.acb.youtubelearningbuddy.utils.AppExecutors;

import java.util.Date;
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
public class TopicsRepository {

    private AppDatabase database;
    private AppExecutors executors;

    @Inject
    TopicsRepository (AppDatabase database, AppExecutors executors) {
        this.database = database;
        this.executors = executors;
    }

    public LiveData<List<TopicEntity>> getTopicsForVideo(final long videoId){
        return database.videoTopicJoinDao().getTopicsForVideo(videoId);
    }

    public LiveData<List<VideoEntity>> getVideosForTopic(final long topicId) {
        return database.videoTopicJoinDao().getVideosForTopic(topicId);
    }

    public LiveData<List<TopicEntity>> getAllTopics() {
        return database.topicDao().getAllTopics();
    }

    public void createTopic(String name) {
        executors.diskIO().execute(() ->
            database.topicDao().insertTopic(new TopicEntity(name, new Date())));
    }

    public void insertVideoTopic(long videoId, long topicId) {
        executors.diskIO().execute(() ->
                database.videoTopicJoinDao().insert(new VideoTopicJoin(videoId, topicId)));
    }

    public void deleteVideoTopic(long videoId, long topicId){
        executors.diskIO().execute(() ->
                database.videoTopicJoinDao().deleteByIds(videoId,topicId));
    }
}
