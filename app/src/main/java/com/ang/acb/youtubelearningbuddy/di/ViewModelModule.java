package com.ang.acb.youtubelearningbuddy.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.youtubelearningbuddy.ui.favorites.FavoriteVideosViewModel;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsViewModel;
import com.ang.acb.youtubelearningbuddy.ui.search.SearchViewModel;
import com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsViewModel;
import com.ang.acb.youtubelearningbuddy.ui.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VideoDetailsViewModel.class)
    abstract ViewModel bindVideoDetailsViewModel(VideoDetailsViewModel videoDetailsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TopicsViewModel.class)
    abstract ViewModel bindTopicsViewModel(TopicsViewModel topicsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteVideosViewModel.class)
    abstract ViewModel bindFavoriteVideosViewModel (FavoriteVideosViewModel favoritesViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
