package com.ang.acb.youtubelearningbuddy.ui.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.model.Resource;
import com.ang.acb.youtubelearningbuddy.data.repository.VideosRepository;
import com.ang.acb.youtubelearningbuddy.utils.AbsentLiveData;
import com.ang.acb.youtubelearningbuddy.utils.Objects;

import java.util.List;
import java.util.Locale;


import javax.inject.Inject;

/**
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 */
public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> query = new MutableLiveData<>();
    private final LiveData<Resource<List<VideoEntity>>> searchResults;

    @Inject
    SearchViewModel(VideosRepository repository) {
        searchResults = Transformations.switchMap(query, search -> {
            if (search == null || search.trim().length() == 0) {
                return AbsentLiveData.create();
            } else {
                return repository.searchVideos(search);
            }
        });
    }

    // FIXME: This stores only last item from result list
    public LiveData<Resource<List<VideoEntity>>> getSearchResults() {
        return searchResults;
    }

    public void setQuery(@NonNull String originalInput) {
        String input = originalInput.toLowerCase(Locale.getDefault()).trim();
        if (Objects.equals(input, query.getValue())) {
            return;
        }
        query.setValue(input);
    }

    void refresh() {
        if (query.getValue() != null) {
            query.setValue(query.getValue());
        }
    }
}
