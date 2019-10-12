package com.ang.acb.youtubelearningbuddy.ui.video;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.youtubelearningbuddy.BuildConfig;
import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.data.vo.Resource;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentVideoDetailsBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class VideoDetailsFragment extends Fragment implements YouTubePlayer.OnInitializedListener {

    public static final String ARG_YOUTUBE_VIDEO_ID = "ARG_YOUTUBE_VIDEO_ID";
    public static final String ARG_ROOM_VIDEO_ID = "ARG_ROOM_VIDEO_ID";

    private String youtubeVideoId;
    private long roomVideoId;
    private FragmentVideoDetailsBinding binding;
    private VideoDetailsViewModel detailsViewModel;
    private TopicsViewModel topicsViewModel;
    private CommentsAdapter commentsAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    // Required empty public constructor
    public VideoDetailsFragment() {}

    public static VideoDetailsFragment newInstance(String youTubeVideoId, long roomVideoId) {
        VideoDetailsFragment fragment = new VideoDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_YOUTUBE_VIDEO_ID, youTubeVideoId);
        args.putLong(ARG_ROOM_VIDEO_ID, roomVideoId);
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
            roomVideoId = getArguments().getLong(ARG_ROOM_VIDEO_ID);
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

        initViewModels();
        displayVideoDetails();
        initYouTubePlayer();
        initCommentsAdapter();
        displayComments();
        handleFavoriteClick();
        handleAddToTopic();
        handleRetryEvents();
    }

    private void initViewModels() {
        detailsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(VideoDetailsViewModel.class);
        detailsViewModel.setVideoId(youtubeVideoId);

        topicsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TopicsViewModel.class);
        topicsViewModel.setVideoId(roomVideoId);
    }

    private void displayVideoDetails() {
        detailsViewModel.getVideo().observe(getViewLifecycleOwner(), video -> {
            detailsViewModel.setFavorite(video.isFavorite());
            binding.setIsFavorite(video.isFavorite());
            binding.setVideo(video);
            setupToolbarTitle(video.getTitle());
        });
    }

    private void setupToolbarTitle(String title) {
        if (getHostActivity().getSupportActionBar() != null) {
            getHostActivity().getSupportActionBar()
                    .setTitle(title);
        }
    }

    private void initCommentsAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.videoDetailsInfo.rvComments.setLayoutManager(layoutManager);
        binding.videoDetailsInfo.rvComments.addItemDecoration(new DividerItemDecoration(
                getContext(), LinearLayoutManager.VERTICAL));
        commentsAdapter = new CommentsAdapter();
        binding.videoDetailsInfo.rvComments.setAdapter(commentsAdapter);
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

    private void handleAddToTopic() {
        binding.icAddToTopics.setOnClickListener(view -> {
            // Navigate to select topics fragment
            Bundle args = new Bundle();
            args.putLong(ARG_ROOM_VIDEO_ID, roomVideoId);
            NavHostFragment.findNavController(VideoDetailsFragment.this)
                    .navigate(R.id.action_video_details_to_select_topics, args);
        });
    }

    private void createTopicsAlertDialog() {
        // See: https://developer.android.com/guide/topics/ui/dialogs.html#Checkboxes
        // FIXME:This can be null
        List<String> allTopicsNames = topicsViewModel.getAllTopicsNames().getValue();
        CharSequence[] allItems = allTopicsNames.toArray(new CharSequence[0]);

        // Track the selected items, include the already existing topics for this video
        List<String> selectedItems = topicsViewModel.getTopicsNamesForVideo().getValue();

        LiveData<List<TopicEntity>> topicsForVideo = topicsViewModel.getTopicsForVideo();

        // Items to be selected by default
        boolean[] checkedItems = new boolean[allTopicsNames.size()];
        int i = 0;
        for (String selectedName: selectedItems) {
            int index = i++;
            checkedItems[index] = allTopicsNames.contains(selectedName);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.save_video_to);

        // Specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive callbacks when items are selected
        dialogBuilder.setMultiChoiceItems(allItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected items.
                    selectedItems.add(allTopicsNames.get(which));
                } else {
                    // Else, if the user un-checked the item, remove it.
                    selectedItems.remove(allTopicsNames.get(which));
                }
            }
        });

        dialogBuilder.setPositiveButton(R.string.dialog_pos_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the selectedItems results.


            }
        });

        dialogBuilder.setNegativeButton(R.string.dialog_neg_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        dialogBuilder.create();
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

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
