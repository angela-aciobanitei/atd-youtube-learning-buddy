package com.ang.acb.youtubelearningbuddy.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Global executor pools for the whole application. Grouping tasks like this avoids
 * the effects of task starvation (e.g. disk reads don't wait behind webservice requests).
 *
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 */
@Singleton
public class AppExecutors {

    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    @Inject
    AppExecutors() {
        this(Executors.newSingleThreadExecutor(),
             Executors.newFixedThreadPool(3),
             new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
