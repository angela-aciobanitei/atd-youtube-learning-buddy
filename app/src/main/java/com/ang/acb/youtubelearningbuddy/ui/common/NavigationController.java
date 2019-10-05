package com.ang.acb.youtubelearningbuddy.ui.common;

import androidx.fragment.app.FragmentManager;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.ui.search.SearchFragment;
import com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment;

import javax.inject.Inject;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class NavigationController {

    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.main_container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToVideoDetails(String youtubeVideoId) {
        VideoDetailsFragment fragment = VideoDetailsFragment.newInstance(youtubeVideoId);
        String tag = "video" + "/" + youtubeVideoId;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}

