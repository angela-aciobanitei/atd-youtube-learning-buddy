package com.ang.acb.youtubelearningbuddy.ui.video;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.youtubelearningbuddy.data.local.entity.CommentEntity;
import com.ang.acb.youtubelearningbuddy.data.model.Resource;
import com.ang.acb.youtubelearningbuddy.data.repository.VideosRepository;
import com.ang.acb.youtubelearningbuddy.utils.AbsentLiveData;

import java.util.List;

import javax.inject.Inject;

public class CommentsViewModel extends ViewModel {

    private final MutableLiveData<String> videoId = new MutableLiveData<>();
    private final LiveData<Resource<List<CommentEntity>>> comments;

    @Inject
    CommentsViewModel(VideosRepository repository) {
        comments = Transformations.switchMap(videoId, id -> {
            if (id == null) return AbsentLiveData.create();
            else return repository.getComments(id);
        });
    }

    public MutableLiveData<String> getVideoId() {
        return videoId;
    }

    public LiveData<Resource<List<CommentEntity>>> getComments() {
        return comments;
    }
}
