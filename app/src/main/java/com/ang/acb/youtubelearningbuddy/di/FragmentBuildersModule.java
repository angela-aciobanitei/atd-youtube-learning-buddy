package com.ang.acb.youtubelearningbuddy.di;

import com.ang.acb.youtubelearningbuddy.ui.pager.ViewPagerFragment;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicDetailsFragment;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsFragment;
import com.ang.acb.youtubelearningbuddy.ui.video.SearchFragment;
import com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ViewPagerFragment contributeViewPagerFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract TopicsFragment contributeTopicsFragment();

    @ContributesAndroidInjector
    abstract TopicDetailsFragment contributeTopicDetailsFragment();

    @ContributesAndroidInjector
    abstract VideoDetailsFragment contributeVideoDetailsFragment();
}
