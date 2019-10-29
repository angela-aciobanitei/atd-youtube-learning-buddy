package com.ang.acb.youtubelearningbuddy.utils;

import androidx.lifecycle.LiveData;

/**
 * A LiveData class that has {@code null} value.
 *
 * See: https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample
 */
public class AbsentLiveData extends LiveData {

    private AbsentLiveData() {
        postValue(null);
    }

    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}
