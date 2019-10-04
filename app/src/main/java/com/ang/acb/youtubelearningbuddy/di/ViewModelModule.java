package com.ang.acb.youtubelearningbuddy.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsViewModel;
import com.ang.acb.youtubelearningbuddy.ui.video.SearchViewModel;
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
    @ViewModelKey(TopicsViewModel.class)
    abstract ViewModel bindTopicsViewModel(TopicsViewModel topicsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
