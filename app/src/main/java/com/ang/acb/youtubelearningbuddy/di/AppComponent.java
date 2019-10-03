package com.ang.acb.youtubelearningbuddy.di;

import android.app.Application;

import com.ang.acb.youtubelearningbuddy.YouTubeApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, BindingModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(YouTubeApplication application);
}
