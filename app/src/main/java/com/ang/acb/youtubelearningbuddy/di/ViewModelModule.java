package com.ang.acb.youtubelearningbuddy.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.youtubelearningbuddy.ui.viewmodel.TopicsViewModel;
import com.ang.acb.youtubelearningbuddy.ui.viewmodel.VideosViewModel;
import com.ang.acb.youtubelearningbuddy.ui.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(VideosViewModel.class)
    abstract ViewModel bindVideosViewModel(VideosViewModel videosViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TopicsViewModel.class)
    abstract ViewModel bindTopicsViewModel(TopicsViewModel topicsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
