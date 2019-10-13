package com.ang.acb.youtubelearningbuddy.ui.favorites;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.repository.VideosRepository;

import java.util.List;

import javax.inject.Inject;

public class FavoriteVideosViewModel extends ViewModel {

    private VideosRepository repository;
    private LiveData<List<VideoEntity>> favorites;

    @Inject
    FavoriteVideosViewModel(VideosRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<VideoEntity>> getFavorites() {
        if (favorites == null) {
            favorites = repository.getAllFavoriteVideos();
        }
        return favorites;
    }
}
