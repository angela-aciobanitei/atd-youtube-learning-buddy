package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentTopicDetailsBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.common.VideosAdapter;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.ang.acb.youtubelearningbuddy.ui.topic.TopicsFragment.ARG_TOPIC_ID;
import static com.ang.acb.youtubelearningbuddy.ui.topic.TopicsFragment.ARG_TOPIC_NAME;
import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_ROOM_VIDEO_ID;
import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_YOUTUBE_VIDEO_ID;

public class TopicDetailsFragment extends Fragment {

    private FragmentTopicDetailsBinding binding;
    private TopicsViewModel topicsViewModel;
    private VideosAdapter videosAdapter;
    private long topicId;
    private String topicName;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public TopicDetailsFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            topicId = getArguments().getLong(ARG_TOPIC_ID);
            topicName = getArguments().getString(ARG_TOPIC_NAME);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get an instance of the binding class.
        binding = FragmentTopicDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupToolbarTitle();
        initViewModel();
        initAdapter();
        populateUi();
    }

    private void setupToolbarTitle() {
        String title = getString(R.string.topic_toolbar_title) + " " + topicName;
        ActionBar actionBar = getHostActivity().getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    private void initViewModel() {
        topicsViewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(TopicsViewModel.class);
        topicsViewModel.setTopicId(topicId);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.rvVideosForTopic.setLayoutManager(layoutManager);
        videosAdapter = new VideosAdapter(this::onVideoClick);
        binding.rvVideosForTopic.setAdapter(videosAdapter);
    }

    private void onVideoClick(VideoEntity videoEntity) {
        Bundle args = new Bundle();
        args.putLong(ARG_ROOM_VIDEO_ID, videoEntity.getId());
        args.putString(ARG_YOUTUBE_VIDEO_ID, videoEntity.getYouTubeVideoId());
        NavHostFragment.findNavController(TopicDetailsFragment.this)
                .navigate(R.id.action_topic_details_to_video_details, args);
    }

    private void populateUi() {
        topicsViewModel.getVideosForTopic().observe(getViewLifecycleOwner(), topicVideos -> {
            // If result is null or empty, display message, else update data.
            int topicVideosCount = (topicVideos == null) ? 0 : topicVideos.size();
            binding.setTopicVideosCount(topicVideosCount);
            if(topicVideosCount != 0) videosAdapter.updateData(topicVideos);
            else binding.topicDetailsEmptyState.setText(R.string.no_videos_for_topic);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        });
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
