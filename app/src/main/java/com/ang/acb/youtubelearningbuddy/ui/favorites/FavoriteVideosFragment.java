package com.ang.acb.youtubelearningbuddy.ui.favorites;


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
import com.ang.acb.youtubelearningbuddy.databinding.FragmentFavoritesBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.common.NavigationController;
import com.ang.acb.youtubelearningbuddy.ui.search.VideosAdapter;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsAdapter;
import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsViewModel;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class FavoriteVideosFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoriteVideosViewModel favoritesViewModel;
    private VideosAdapter videosAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;


    // Required empty public constructor
    public FavoriteVideosFragment() {}

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
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
        if (getHostActivity().getSupportActionBar() != null) {
            getHostActivity().getSupportActionBar()
                    .setTitle(getString(R.string.action_show_favorites));
        }
    }

    private void initViewModel() {
        favoritesViewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(FavoriteVideosViewModel.class);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.rvFavorites.setLayoutManager(layoutManager);
        binding.rvFavorites.addItemDecoration(new DividerItemDecoration(
                getContext(), LinearLayoutManager.VERTICAL));
        videosAdapter = new VideosAdapter(videoItem ->
                navigationController.navigateToVideoDetails(videoItem.getYouTubeVideoId()));
        binding.rvFavorites.setAdapter(videosAdapter);
    }

    private void populateUi() {
        favoritesViewModel.getFavoriteListLiveData().observe(getViewLifecycleOwner(), result -> {
            binding.setFavoritesCount((result == null) ? 0 : result.size());
            if (result != null) {
                videosAdapter.submitList(result);
            }
            binding.executePendingBindings();
        });
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}