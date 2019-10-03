package com.ang.acb.youtubelearningbuddy.di;

import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BindingModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();
}
