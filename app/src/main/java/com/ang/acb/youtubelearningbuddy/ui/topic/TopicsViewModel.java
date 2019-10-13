package com.ang.acb.youtubelearningbuddy.ui.topic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.repository.TopicsRepository;
import com.ang.acb.youtubelearningbuddy.utils.AbsentLiveData;

import java.util.List;

import javax.inject.Inject;

/**
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 */
public class TopicsViewModel extends ViewModel {

    private final MutableLiveData<Long> videoId = new MutableLiveData<>();
    private final MutableLiveData<Long> topicId = new MutableLiveData<>();
    private TopicsRepository repository;


    @Inject
    TopicsViewModel(TopicsRepository repository) {
        this.repository = repository;
    }

    public void setVideoId(long value) {
        videoId.setValue(value);
    }

    public void setTopicId(long value) {
        topicId.setValue(value);
    }

    public LiveData<List<TopicEntity>> getTopicsForVideo() {
        return Transformations.switchMap(videoId, id -> {
            if (id == null) return AbsentLiveData.create();
            else return repository.getTopicsForVideo(id);
        });
    }

    public LiveData<List<VideoEntity>> getVideosForTopic() {
        return Transformations.switchMap(topicId, id -> {
            if (id == null) return AbsentLiveData.create();
            else return repository.getVideosForTopic(id);
        });
    }

    public LiveData<List<TopicEntity>> getAllTopics() {
        return repository.getAllTopics();
    }

    public void createTopic(String name) {
        repository.createTopic(name);
    }

    public void insertVideoTopic(long videoId, long topicId) {
        repository.insertVideoTopic(videoId, topicId);
    }

    public void deleteVideoTopic(long videoId, long topicId) {
        repository.deleteByIds(videoId, topicId);
    }
}
