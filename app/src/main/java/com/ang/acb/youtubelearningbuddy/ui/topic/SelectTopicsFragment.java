package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentTopicSelectBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.utils.UiUtils;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_ROOM_VIDEO_ID;

public class SelectTopicsFragment extends Fragment {

    private FragmentTopicSelectBinding binding;
    private TopicsViewModel topicsViewModel;
    private SelectTopicsAdapter selectAdapter;
    private long roomVideoId;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public SelectTopicsFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger for injecting Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            roomVideoId = getArguments().getLong(ARG_ROOM_VIDEO_ID);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get an instance of the binding class.
        binding = FragmentTopicSelectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
        initAdapter();
        handleNewTopicCreation();
        populateUi();
    }

    private void initViewModel() {
        topicsViewModel = ViewModelProviders.of(getHostActivity(), viewModelFactory)
                .get(TopicsViewModel.class);
        topicsViewModel.setVideoId(roomVideoId);
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.rvTopicsSelect.setLayoutManager(layoutManager);
        selectAdapter = new SelectTopicsAdapter(getCallback());
        binding.rvTopicsSelect.setAdapter(selectAdapter);
    }

    private SelectTopicsAdapter.SelectTopicCallback getCallback() {
        return new SelectTopicsAdapter.SelectTopicCallback() {
            @Override
            public void onTopicChecked(TopicEntity topicEntity) {
                // Save the result into the database and show a snackbar message.
                topicsViewModel.onTopicChecked(roomVideoId, topicEntity.getId());
            }

            @Override
            public void onTopicUnchecked(TopicEntity topicEntity) {
                // Delete item from the database and show a snackbar message.
                topicsViewModel.onTopicUnchecked(roomVideoId, topicEntity.getId());
            }
        };
    }

    private void handleNewTopicCreation() {
        binding.newTopicButton.setOnClickListener(view ->
                UiUtils.createNewTopicDialog(getHostActivity(), topicsViewModel));
    }

    private void populateUi() {
        // Get all existing topics from the database.
        topicsViewModel.getAllTopics().observe(getViewLifecycleOwner(), allTopics -> {
            if (allTopics != null) {
                // Get topics associated with this particular video.
                topicsViewModel.getTopicsForVideo().observe(getViewLifecycleOwner(), videoTopics -> {
                    if (videoTopics != null) {
                        selectAdapter.updateData(allTopics, getTopicStates(allTopics, videoTopics));
                        binding.executePendingBindings();
                    }
                });
            }
        });

        // Observe the Snackbar messages displayed when adding/removing video from favorites.
        topicsViewModel.getSnackbarMessage().observe(this, (Observer<Integer>) message ->
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show());
    }

    private LongSparseArray<Boolean> getTopicStates(final List<TopicEntity> allTopics,
                                                    final List<TopicEntity> videoTopics) {
        // Maps longs (the topic IDs) to booleans (are topics checked or unchecked).
        // It's more memory efficient than a HashMap<Long,Boolean>.
        LongSparseArray<Boolean> topicStates = new LongSparseArray<>();
        for (TopicEntity topic: allTopics) {
            boolean isAssignedToVideo = false;
            for (TopicEntity videoTopic: videoTopics) {
                if(topic.getId() == videoTopic.getId()){
                    isAssignedToVideo = true;
                    break;
                }
            }
            topicStates.put(topic.getId(), isAssignedToVideo);
        }

        return  topicStates;
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
