package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.youtubelearningbuddy.R;
import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.databinding.FragmentTopicsBinding;
import com.ang.acb.youtubelearningbuddy.ui.common.MainActivity;
import com.ang.acb.youtubelearningbuddy.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class TopicsFragment extends Fragment {

    public static final String ARG_TOPIC_ID = "ARG_TOPIC_ID";
    public static final String ARG_TOPIC_NAME = "ARG_TOPIC_NAME";

    private FragmentTopicsBinding binding;
    private TopicsViewModel topicsViewModel;
    private TopicsAdapter topicsAdapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public TopicsFragment() {}

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
        binding = FragmentTopicsBinding.inflate(inflater, container, false);
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
    }

    private void initAdapter(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        binding.rvTopics.setLayoutManager(layoutManager);
        topicsAdapter = new TopicsAdapter(this::onTopicClick);
        binding.rvTopics.setAdapter(topicsAdapter);
    }

    private void onTopicClick(TopicEntity topicEntity) {
        // On item click navigate to topic details fragment
        // and send the topic ID and topic name as bundle arguments.
        Bundle args = new Bundle();
        args.putLong(ARG_TOPIC_ID, topicEntity.getId());
        args.putString(ARG_TOPIC_NAME, topicEntity.getName());
        NavHostFragment.findNavController(TopicsFragment.this)
                .navigate(R.id.action_topic_list_to_topic_details, args);
    }

    private void handleNewTopicCreation() {
        binding.newTopicButton.setOnClickListener(view ->
                UiUtils.createNewTopicDialog(getHostActivity(), topicsViewModel));
    }

    private void populateUi() {
        topicsViewModel.getAllTopics().observe(getViewLifecycleOwner(), result -> {
            int topicsCount = (result == null) ? 0 : result.size();
            binding.setTopicsCount(topicsCount);
            if (topicsCount != 0) topicsAdapter.submitList(result);
            else binding.topicsEmptyState.setText(R.string.no_topics);

            binding.executePendingBindings();
        });
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }
}
