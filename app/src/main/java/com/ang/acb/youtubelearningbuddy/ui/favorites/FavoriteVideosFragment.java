package com.ang.acb.youtubelearningbuddy.ui.favorites;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.ang.acb.youtubelearningbuddy.databinding.FragmentFavoritesBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.common.VideosAdapter;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_ROOM_VIDEO_ID;
import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_YOUTUBE_VIDEO_ID;

public class FavoriteVideosFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoriteVideosViewModel favoritesViewModel;
    private VideosAdapter videosAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public FavoriteVideosFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger for injecting Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get an instance of the binding class.
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
        onVideoSwiped();
    }

    private void setupToolbarTitle() {
        if (getHostActivity().getSupportActionBar() != null) {
            getHostActivity().getSupportActionBar().setTitle(getString(R.string.favorites));
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
        videosAdapter = new VideosAdapter(this::onVideoClick);
        binding.rvFavorites.setAdapter(videosAdapter);
    }

    private void onVideoClick(VideoEntity videoEntity) {
        Bundle args = new Bundle();
        args.putString(ARG_YOUTUBE_VIDEO_ID, videoEntity.getYouTubeVideoId());
        args.putLong(ARG_ROOM_VIDEO_ID, videoEntity.getId());
        NavHostFragment.findNavController(FavoriteVideosFragment.this)
                .navigate(R.id.action_favorites_to_video_details, args);
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

                favoritesViewModel.deleteVideo(video);
            }
        });

        // Attach the item touch helper to the recycler view
        helper.attachToRecyclerView(binding.rvFavorites);
    }

    private void populateUi() {
        favoritesViewModel.getFavorites().observe(getViewLifecycleOwner(), result -> {
            // If result is null or empty, display message, else update data.
            int favoritesCount = (result == null) ? 0 : result.size();
            if(favoritesCount != 0) videosAdapter.updateData(result);
            else binding.favoritesEmptyState.setText(R.string.no_favorites);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        });
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
