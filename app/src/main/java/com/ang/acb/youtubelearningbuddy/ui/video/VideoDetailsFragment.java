package com.ang.acb.youtubelearningbuddy.ui.video;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ang.acb.youtubelearningbuddy.BuildConfig;
import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.model.Resource;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentVideoDetailsBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.NavigationController;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class VideoDetailsFragment extends Fragment implements YouTubePlayer.OnInitializedListener {

    private static final String ARG_YOUTUBE_VIDEO_ID = "ARG_YOUTUBE_VIDEO_ID";

    private String youtubeVideoId;
    private FragmentVideoDetailsBinding binding;
    private VideoDetailsViewModel detailsViewModel;
    private CommentsAdapter commentsAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    // Required empty public constructor
    public VideoDetailsFragment() {}

    public static VideoDetailsFragment newInstance(String youTubeVideoId) {
        VideoDetailsFragment fragment = new VideoDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_YOUTUBE_VIDEO_ID, youTubeVideoId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        // Note: when using Dagger for injecting Fragment objects,
        // inject as early as possible. For this reason, call
        // AndroidInjection.inject() in onAttach(). This also
        // prevents inconsistencies if the Fragment is reattached.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            youtubeVideoId = getArguments().getString(ARG_YOUTUBE_VIDEO_ID);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        binding = FragmentVideoDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
        initAdapter();
        initYouTubePlayer();
        displayComments();
        displayVideoDetails();
    }

    private void initViewModel() {
        detailsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(VideoDetailsViewModel.class);
        detailsViewModel.setVideoId(youtubeVideoId);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.rvComments.setLayoutManager(layoutManager);
        binding.rvComments.addItemDecoration(new DividerItemDecoration(
                getContext(), LinearLayoutManager.VERTICAL));
        commentsAdapter = new CommentsAdapter();
        binding.rvComments.setAdapter(commentsAdapter);
    }

    private void displayVideoDetails() {
        detailsViewModel.getVideo().observe(getViewLifecycleOwner(), video -> {
            binding.setVideo(video);
        });
    }

    private void displayComments() {
        detailsViewModel.getComments().observe(getViewLifecycleOwner(), result -> {
            binding.setResource(result);
            int commentsCount = (result == null || result.data == null) ? 0 : result.data.size();
            if (result != null && result.status == Resource.Status.SUCCESS && commentsCount == 0){
                binding.noResultsText.setText(getString(R.string.no_comments_for_this_video));
            }
            if (result!= null && result.message != null && result.message.contains("disabled comments")) {
                binding.noResultsText.setText(getString(R.string.comments_disabled));
                binding.networkState.rootLinearLayout.setVisibility(View.GONE);
            }
            if(result != null && result.data != null) {
                commentsAdapter.submitList(result.data);
            }
            binding.executePendingBindings();
        });
    }

    private void initYouTubePlayer() {
        // See: https://stackoverflow.com/questions/19848142/how-to-load-youtubeplayer-using-youtubeplayerfragment-inside-another-fragment
        // See: https://gist.github.com/medyo/f226b967213c3b8ec6f6bebb5338a492
        YouTubePlayerSupportFragmentX youTubeFragment = YouTubePlayerSupportFragmentX.newInstance();
        youTubeFragment.initialize(BuildConfig.YOU_TUBE_API_KEY, this);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.video_container, youTubeFragment)
                .commit();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setShowFullscreenButton(false);
        youTubePlayer.loadVideo(youtubeVideoId);
        youTubePlayer.play();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.topic_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle menu item selection
        switch (item.getItemId()) {
            case R.id.action_add_to_topic:
                Toast.makeText(getActivity(), "Add to Topic", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_remove_from_topic:
                Toast.makeText(getActivity(), "Remove from Topic", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
