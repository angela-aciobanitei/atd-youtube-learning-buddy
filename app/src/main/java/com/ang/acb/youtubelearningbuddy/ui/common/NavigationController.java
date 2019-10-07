package com.ang.acb.youtubelearningbuddy.ui.common;

import androidx.fragment.app.FragmentManager;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.ui.search.SearchFragment;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicDetailsFragment;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsFragment;
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
        this.containerId = R.id.main_fragment_container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        fragmentManager.beginTransaction()
                .replace(containerId, new SearchFragment())
                .commitAllowingStateLoss();
    }

    public void navigateToTopics() {
        fragmentManager.beginTransaction()
                .replace(containerId, new TopicsFragment())
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

    public void navigateToTopicDetails(long topicId) {
        fragmentManager.beginTransaction()
                .replace(containerId, new TopicDetailsFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}

