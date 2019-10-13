package com.ang.acb.youtubelearningbuddy.ui.topic;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.databinding.TopicSelectItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SelectTopicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private TopicCheckedCallback checkedCallback;
    private List<TopicEntity> topicList;
    private LinkedHashMap<TopicEntity, Boolean> topicStates;

    public SelectTopicsAdapter(TopicCheckedCallback checkedCallback) {
        this.checkedCallback = checkedCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout and get an instance of the binding class.
        TopicSelectItemBinding itemBinding = TopicSelectItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new TopicSelectViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind item data
        ((TopicSelectViewHolder) holder).bindTo(topicList.get(position));
    }

    @Override
    public int getItemCount() {
        return topicList == null ? 0 :  topicList.size();
    }

    public void updateData(LinkedHashMap<TopicEntity, Boolean> topics) {
        topicList = new ArrayList<>();
        topicList.addAll(topics.keySet());
        topicStates = topics;
        // Notify any registered observers
        // that the data set has changed.
        notifyDataSetChanged();
    }

    class TopicSelectViewHolder extends RecyclerView.ViewHolder {

        private TopicSelectItemBinding binding;

        // Required constructor matching super
        TopicSelectViewHolder(@NonNull TopicSelectItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(TopicEntity topicEntity) {
            // Bind data for this item.
            binding.setTopic(topicEntity);

            binding.selectTopicCheckbox.setOnCheckedChangeListener(null);

            binding.selectTopicCheckbox.setChecked(topicStates.get(topicEntity));

            // Register a callback to be invoked when the checked state of this button changes.
            binding.selectTopicCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (topicEntity != null && checkedCallback != null) {
                    if (isChecked) checkedCallback.onTopicChecked(topicEntity);
                    else checkedCallback.onTopicUnchecked(topicEntity);
                }
            });

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }

    public interface TopicCheckedCallback {
        void onTopicChecked(TopicEntity topicEntity);
        void onTopicUnchecked(TopicEntity topicEntity);
    }
}
