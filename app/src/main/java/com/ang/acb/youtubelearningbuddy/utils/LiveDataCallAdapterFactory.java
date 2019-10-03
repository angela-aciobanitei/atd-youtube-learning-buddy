package com.ang.acb.youtubelearningbuddy.utils;

import androidx.lifecycle.LiveData;

import com.ang.acb.youtubelearningbuddy.data.remote.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * A factory class for creating a Retrofit call adapter that
 * converts the Retrofit2.Call into a LiveData of ApiResponse.
 *
 * See: https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample
 */
public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType != ApiResponse.class) {
            throw new IllegalArgumentException("Type must be a resource");
        }
        if (! (observableType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Resource must be parameterized");
        }
        Type bodyType = getParameterUpperBound(0, (ParameterizedType) observableType);
        return new LiveDataCallAdapter<>(bodyType);
    }
}
