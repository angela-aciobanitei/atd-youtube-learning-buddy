package com.ang.acb.youtubelearningbuddy.di;

import com.ang.acb.youtubelearningbuddy.ui.favorites.FavoriteVideosFragment;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicDetailsFragment;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicSelectFragment;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsFragment;
import com.ang.acb.youtubelearningbuddy.ui.search.SearchFragment;
import com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract TopicsFragment contributeTopicsFragment();

    @ContributesAndroidInjector
    abstract TopicDetailsFragment contributeTopicDetailsFragment();

    @ContributesAndroidInjector
    abstract TopicSelectFragment contributeTopicSelectFragment();

    @ContributesAndroidInjector
    abstract VideoDetailsFragment contributeVideoDetailsFragment();

    @ContributesAndroidInjector
    abstract FavoriteVideosFragment contributeFavoriteVideosFragment();
}
