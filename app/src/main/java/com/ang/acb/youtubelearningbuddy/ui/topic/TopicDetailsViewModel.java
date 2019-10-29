package com.ang.acb.youtubelearningbuddy.ui.topic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.repository.TopicsRepository;
import com.ang.acb.youtubelearningbuddy.data.repository.VideosRepository;
import com.ang.acb.youtubelearningbuddy.utils.AbsentLiveData;

import java.util.List;

import javax.inject.Inject;


public class TopicDetailsViewModel extends ViewModel {

    private TopicsRepository topicsRepository;
    private VideosRepository videosRepository;
    private final MutableLiveData<Long> topicId = new MutableLiveData<>();

    @Inject
    TopicDetailsViewModel(TopicsRepository topicsRepository, VideosRepository videosRepository) {
        this.topicsRepository = topicsRepository;
        this.videosRepository = videosRepository;
    }

    public void setTopicId(long value) {
        topicId.setValue(value);
    }

    public LiveData<List<VideoEntity>> getVideosForTopic() {
        return Transformations.switchMap(topicId, id -> {
            if (id == null) return AbsentLiveData.create();
            else return topicsRepository.getVideosForTopic(id);
        });
    }

    public void deleteVideo(VideoEntity videoEntity) {
        videosRepository.deleteVideo(videoEntity);
    }
}
