package com.ang.acb.youtubelearningbuddy.ui.video;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.model.Resource;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentVideoDetailsBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.NavigationController;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class VideoDetailsFragment extends Fragment {

    private static final String ARG_YOUTUBE_VIDEO_ID = "ARG_YOUTUBE_VIDEO_ID";

    private String youtubeVideoId;
    private FragmentVideoDetailsBinding binding;
    private CommentsViewModel commentsViewModel;
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
        displayComments();
    }

    private void initViewModel() {
        commentsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CommentsViewModel.class);
        commentsViewModel.setVideoId(youtubeVideoId);
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

    private void displayComments() {
        commentsViewModel.getComments().observe(getViewLifecycleOwner(), result -> {
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
}
