package com.ang.acb.youtubelearningbuddy.data.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.ang.acb.youtubelearningbuddy.data.model.Resource;
import com.ang.acb.youtubelearningbuddy.data.remote.ApiResponse;
import com.ang.acb.youtubelearningbuddy.utils.AppExecutors;
import com.ang.acb.youtubelearningbuddy.utils.Objects;


/**
 * A generic class that can provide a resource backed by both the SQLite database and the network.
 * It defines two type parameters, ResultType and RequestType, because the data type returned from
 * the API might not match the data type used locally.
 *
 * See: https://developer.android.com/jetpack/docs/guide#addendum.
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 *
 * @param <ResultType> Type for the Resource data.
 * @param <RequestType> Type for the API response.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {

    // The final result LiveData. Note: why use MediatorLiveData? Consider the following
    // scenario: we have 2 instances of LiveData, let's name them liveData1 and liveData2,
    // and we want to merge their emissions in one object: liveDataMerger. Then, liveData1
    // and liveData2 will become sources for the MediatorLiveData liveDataMerger and every
    // time onChanged is called for either of them, we set a new value in liveDataMerger.
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    private AppExecutors appExecutors;

    @MainThread
    public NetworkBoundResource(AppExecutors appExecutors) {
        // Init app executors.
        this.appExecutors = appExecutors;

        // Send loading state to the UI.
        result.setValue(Resource.loading(null));

        // Get the cached data from the database.
        final LiveData<ResultType> dbSource = loadFromDb();

        // Attach the database LiveData as a new source.
        result.addSource(dbSource, data -> {
            // The source value was changed, so we can stop
            // listening to the database LiveData.
            result.removeSource(dbSource);
            // Decide whether to fetch potentially updated data from the network.
            if (shouldFetch(data)) {
                // Fetch data from network, persist it into DB
                // and then send it back to the UI.
                fetchFromNetwork(dbSource);
            } else {
                // Re-attach the database LiveData as a new source.
                result.addSource(dbSource, newData ->

                        setValue(Resource.success(newData)));
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    /**
     * Fetch the data from network and persist into DB and then send it back to UI.
     */
    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        // Create the API call. Note the use of ApiResponse, the generic class we've
        // created earlier, which consists of an HTTP status code, some data and an error.
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        // Re-attach the database LiveData as a new source,
        // it will dispatch its latest value quickly.
        result.addSource(dbSource, newData -> setValue(Resource.loading(newData)));

        // Attach the API response as a new source.
        result.addSource(apiResponse, response -> {
            // The source value was changed, so we can stop listening
            // to both the API response LiveData and database LiveData.
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            // If the network call completes successfully, save the response
            // into the database and re-initialize the stream.
            if (response.isSuccessful()) {
                appExecutors.diskIO().execute(() -> {
                    saveCallResult(processResponse(response));
                    appExecutors.mainThread().execute(() -> {
                        // We specially request new live data, otherwise we will
                        // get the immediately last cached value, which may not
                        // be updated with latest results received from network.
                        result.addSource(loadFromDb(), newData ->
                                setValue(Resource.success(newData)));
                    });
                });
            }
            // If network request fails, dispatch a failure directly.
            else {
                onFetchFailed();
                result.addSource(dbSource, newData ->
                        setValue(Resource.error(response.getError().getMessage(), newData)));
            }
        });
    }

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called when the fetch fails. The child class may want to reset
    // components like rate limiter.
    @NonNull
    @MainThread
    protected abstract void onFetchFailed();

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Returns a LiveData object that represents the resource that's
    // implemented in the base class.
    public final LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }
}