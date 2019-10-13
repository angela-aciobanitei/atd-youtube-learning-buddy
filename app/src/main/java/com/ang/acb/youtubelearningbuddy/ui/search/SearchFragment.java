package com.ang.acb.youtubelearningbuddy.ui.search;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentSearchBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.ui.common.VideosAdapter;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_ROOM_VIDEO_ID;
import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_YOUTUBE_VIDEO_ID;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;
    private VideosAdapter videosAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public SearchFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // Note: when using Dagger for injecting Fragment objects, inject as early
        // as possible. For this reason, call AndroidInjection.inject() in onAttach().
        // This also prevents inconsistencies if the Fragment is reattached.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get an instance of the binding class.
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupToolbarTitle();
        initViewModel();
        initAdapter();
        populateUi();
        initSearchInputListener();
        handleRetryEvents();
    }

    private void setupToolbarTitle() {
        if (getHostActivity().getSupportActionBar() != null) {
            getHostActivity().getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }

    private void initViewModel() {
        searchViewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(SearchViewModel.class);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.rvVideos.setLayoutManager(layoutManager);
        videosAdapter = new VideosAdapter(this::onVideoClick);
        binding.rvVideos.setAdapter(videosAdapter);
    }

    private void onVideoClick(VideoEntity videoEntity) {
        Bundle args = new Bundle();
        args.putString(ARG_YOUTUBE_VIDEO_ID, videoEntity.getYouTubeVideoId());
        args.putLong(ARG_ROOM_VIDEO_ID, videoEntity.getId());
        NavHostFragment.findNavController(SearchFragment.this)
                .navigate(R.id.action_search_to_video_details, args);
    }

    private void initSearchInputListener() {
        binding.input.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view);
                return true;
            }
            return false;
        });

        binding.input.setOnKeyListener((view, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                doSearch(view);
                return true;
            }
            return false;
        });
    }

    private void doSearch(View view) {
        String query = binding.input.getText().toString();
        dismissKeyboard(view.getWindowToken());
        binding.setQuery(query);
        searchViewModel.setQuery(query);
    }

    private void dismissKeyboard(IBinder windowToken) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    private void populateUi() {
        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), result -> {
            binding.setResource(result);
            int searchCount = (result == null || result.data == null) ? 0 : result.data.size();
            binding.setSearchCount(searchCount);
            if (result != null && result.data != null) {
                videosAdapter.updateData(result.data);
            }
            binding.executePendingBindings();
        });
    }

    private void handleRetryEvents() {
        // Handle retry event in case of network failure.
        binding.setRetryCallback(() -> searchViewModel.retry());
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
