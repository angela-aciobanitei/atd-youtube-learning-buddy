package com.ang.acb.youtubelearningbuddy.ui.topic;

import androidx.lifecycle.ViewModel;

import com.ang.acb.youtubelearningbuddy.data.repository.VideosRepository;

import javax.inject.Inject;

/**
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 * See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
 * See: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
public class TopicsViewModel extends ViewModel {

    @Inject
    public TopicsViewModel(VideosRepository repository) {
    }
}
