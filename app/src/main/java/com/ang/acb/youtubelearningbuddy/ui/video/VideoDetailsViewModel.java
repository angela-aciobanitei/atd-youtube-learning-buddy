package com.ang.acb.youtubelearningbuddy.ui.video;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.local.entity.CommentEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.vo.Resource;
import com.ang.acb.youtubelearningbuddy.data.repository.VideosRepository;
import com.ang.acb.youtubelearningbuddy.utils.AbsentLiveData;
import com.ang.acb.youtubelearningbuddy.utils.SnackbarMessage;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 */
public class VideoDetailsViewModel extends ViewModel {

    private VideosRepository repository;
    private final MutableLiveData<String> videoId = new MutableLiveData<>();
    private LiveData<VideoEntity> video;
    private final LiveData<Resource<List<CommentEntity>>> comments;
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private boolean isFavorite;

    @Inject
    VideoDetailsViewModel(VideosRepository repository) {
        this.repository = repository;

        video = Transformations.switchMap(videoId, id -> {
            if (id == null) return AbsentLiveData.create();
            else return repository.getVideoDetails(id);
        });

        comments = Transformations.switchMap(videoId, id -> {
            if (id == null) return AbsentLiveData.create();
            else return repository.getComments(id);
        });
    }

    public void setVideoId(String value) {
        videoId.setValue(value);
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void retry(String value) {
        String id = videoId.getValue();
        if (id != null && !id.isEmpty()){
            videoId.setValue(value);
        }
    }

    public LiveData<Resource<List<CommentEntity>>> getComments() {
        return comments;
    }

    public LiveData<VideoEntity> getVideo() {
        return video;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }


    public void onFavoriteClicked() {
        VideoEntity videoEntity = Objects.requireNonNull(video.getValue());
        if (!isFavorite) {
            repository.markAsFavorite(Objects.requireNonNull(videoEntity));
            snackbarMessage.setValue(R.string.video_added_to_favorites);
            isFavorite = true;
        } else {
            repository.markAsNotFavorite(Objects.requireNonNull(videoEntity));
            snackbarMessage.setValue(R.string.video_removed_from_favorites);
            isFavorite = false;
        }
    }
}
