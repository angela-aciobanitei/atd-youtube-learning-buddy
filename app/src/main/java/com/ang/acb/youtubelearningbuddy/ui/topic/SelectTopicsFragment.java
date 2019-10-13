package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentTopicSelectBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.ang.acb.youtubelearningbuddy.ui.video.VideoDetailsFragment.ARG_ROOM_VIDEO_ID;

public class SelectTopicsFragment extends Fragment {

    private FragmentTopicSelectBinding binding;
    private TopicsViewModel topicsViewModel;
    private SelectTopicsAdapter selectAdapter;
    private long roomVideoId;
    private List<TopicEntity> selectedTopics = new ArrayList<>();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public SelectTopicsFragment() {}

    public static SelectTopicsFragment newInstance(long roomVideoId) {
        SelectTopicsFragment fragment = new SelectTopicsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ROOM_VIDEO_ID, roomVideoId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger for injecting Fragments,
        // inject as early as possible.
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
        // Inflate the layout for this fragment
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
        handleConfirm();
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
        selectAdapter = new SelectTopicsAdapter(new SelectTopicsAdapter.TopicCheckedCallback() {
            @Override
            public void onTopicChecked(TopicEntity topicEntity) {
                //selectedTopics.add(topicEntity);
            }

            @Override
            public void onTopicUnchecked(TopicEntity topicEntity) {
                //selectedTopics.remove(topicEntity);
            }
        });
        binding.rvTopicsSelect.setAdapter(selectAdapter);
    }

    private void handleNewTopicCreation() {
        binding.newTopicButton.setOnClickListener(view ->
                UiUtils.createNewTopicDialog(getHostActivity(), topicsViewModel));
    }

    private void populateUi() {
        LinkedHashMap<TopicEntity, Boolean> topicMap = new LinkedHashMap<>();
        topicsViewModel.getAllTopics().observe(getViewLifecycleOwner(), allTopics -> {
            if (allTopics != null) {
                topicsViewModel.getTopicsForVideo().observe(getViewLifecycleOwner(), videoTopics -> {
                    if (videoTopics != null) {
                        for (TopicEntity topic: allTopics) {
                            boolean isAssignedToVideo = false;
                            for (TopicEntity videoTopic: videoTopics) {
                                if(topic.getId() == videoTopic.getId()){
                                    isAssignedToVideo = true;
                                    break;
                                }
                            }
                            topicMap.put(topic, isAssignedToVideo);
                        }
                        selectAdapter.updateData(topicMap);
                        binding.executePendingBindings();
                    }
                });
            }
        });

    }

    private void handleConfirm() {
        binding.buttonConfirm.setOnClickListener(view -> {
            // Get all checked topic items and insert the result into the db
            for(TopicEntity topic : selectedTopics) {
                topicsViewModel.insertVideoTopic(roomVideoId, topic.getId());
            }

            // Navigate back to video details fragment
            NavHostFragment.findNavController(SelectTopicsFragment.this).popBackStack();
        });


    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }

}
