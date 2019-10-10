package com.ang.acb.youtubelearningbuddy.ui.video;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ang.acb.youtubelearningbuddy.BuildConfig;
import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.vo.Resource;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentVideoDetailsBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class VideoDetailsFragment extends Fragment implements YouTubePlayer.OnInitializedListener {

    public static final String ARG_YOUTUBE_VIDEO_ID = "ARG_YOUTUBE_VIDEO_ID";

    private String youtubeVideoId;
    private FragmentVideoDetailsBinding binding;
    private VideoDetailsViewModel detailsViewModel;
    private CommentsAdapter commentsAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


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
        handleFavoriteClick();
        handleRetryEvents();
    }

    private void initViewModel() {
        detailsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(VideoDetailsViewModel.class);
        detailsViewModel.setVideoId(youtubeVideoId);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.videoDetailsInfo.rvComments.setLayoutManager(layoutManager);
        binding.videoDetailsInfo.rvComments.addItemDecoration(new DividerItemDecoration(
                getContext(), LinearLayoutManager.VERTICAL));
        commentsAdapter = new CommentsAdapter();
        binding.videoDetailsInfo.rvComments.setAdapter(commentsAdapter);
    }

    private void displayVideoDetails() {
        detailsViewModel.getVideo().observe(getViewLifecycleOwner(), video -> {
            detailsViewModel.setFavorite(video.isFavorite());
            binding.setIsFavorite(video.isFavorite());
            binding.setVideo(video);
            // setupToolbarTitle(video.getTitle());
        });
    }

    private void handleRetryEvents() {
        // Handle retry event in case of network failure.
        binding.setRetryCallback(() -> detailsViewModel.retry(youtubeVideoId));
    }

    private void handleFavoriteClick() {
        binding.icFavorites.setOnClickListener(view -> {
            detailsViewModel.onFavoriteClicked();
        });

        // Observe the Snackbar messages displayed when adding/removing video from favorites.
        detailsViewModel.getSnackbarMessage().observe(this, (Observer<Integer>) message ->
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show());
    }

    private void displayComments() {
        detailsViewModel.getComments().observe(getViewLifecycleOwner(), result -> {
            binding.setResource(result);
            int commentsCount = (result == null || result.data == null) ? 0 : result.data.size();
            binding.setCommentsCount(commentsCount);
            if (result != null && result.status == Resource.Status.SUCCESS && commentsCount == 0){
                binding.videoDetailsInfo.noResultsText.setText(getString(R.string.no_comments_for_this_video));
            }
            if (result!= null && result.message != null && result.message.contains("disabled comments")) {
                binding.videoDetailsInfo.noResultsText.setText(getString(R.string.comments_disabled));
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
        // See: https://stackoverflow.com/questions/52577000/youtube-player-support-fragment-no-longer-working-on-android-studio-3-2-android
        YouTubePlayerSupportFragmentX youTubeFragment = YouTubePlayerSupportFragmentX.newInstance();
        youTubeFragment.initialize(BuildConfig.YOU_TUBE_API_KEY, this);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.video_container, youTubeFragment)
                .commit();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.loadVideo(youtubeVideoId);
        youTubePlayer.play();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        Snackbar.make(binding.getRoot(),
                getString(R.string.player_init_failed),
                Snackbar.LENGTH_SHORT)
                .show();
    }

    private void setupToolbarTitle(String title) {
        if (getHostActivity().getSupportActionBar() != null) {
            getHostActivity().getSupportActionBar()
                    .setTitle(title);
        }
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
