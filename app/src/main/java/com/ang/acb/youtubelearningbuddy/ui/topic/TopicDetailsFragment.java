package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentTopicDetailsBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.common.VideosAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.ang.acb.youtubelearningbuddy.ui.topic.TopicsFragment.ARG_TOPIC_ID;
import static com.ang.acb.youtubelearningbuddy.ui.topic.TopicsFragment.ARG_TOPIC_NAME;
import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_ROOM_VIDEO_ID;
import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_YOUTUBE_VIDEO_ID;

public class TopicDetailsFragment extends Fragment {

    private FragmentTopicDetailsBinding binding;
    private TopicDetailsViewModel topicDetailsViewModel;
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
        onVideoSwiped();
    }

    private void setupToolbarTitle() {
        String title = getString(R.string.topic_toolbar_title) + " " + topicName;
        ActionBar actionBar = getHostActivity().getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    private void initViewModel() {
        topicDetailsViewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(TopicDetailsViewModel.class);
        topicDetailsViewModel.setTopicId(topicId);
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

    private void onVideoSwiped() {
        // See: https://github.com/google-developer-training/android-advanced/tree/master/RoomWordsWithDelete
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView,
                                  @NotNull RecyclerView.ViewHolder viewHolder,
                                  @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            // When the user swipes a topic, delete that topic from the database.
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Get the position of the ViewHolder that was swiped.
                int position = viewHolder.getAdapterPosition();
                VideoEntity video = videosAdapter.getVideoAt(position);
                Toast.makeText(getContext(),
                              getString(R.string.delete_preamble) + " " +
                               video.getTitle(), Toast.LENGTH_LONG).show();

                topicDetailsViewModel.deleteVideo(video);
            }
        });

        // Attach the item touch helper to the recycler view
        helper.attachToRecyclerView(binding.rvVideosForTopic);
    }

    private void populateUi() {
        topicDetailsViewModel.getVideosForTopic().observe(getViewLifecycleOwner(), topicVideos -> {
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
