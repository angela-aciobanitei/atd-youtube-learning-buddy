package com.ang.acb.youtubelearningbuddy.ui.topic;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.databinding.TopicSelectItemBinding;

import java.util.List;

public class SelectTopicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface TopicCheckedCallback {
        void onTopicChecked(TopicEntity topicEntity);
        void onTopicUnchecked(TopicEntity topicEntity);
    }

    private List<TopicEntity> topics;
    private TopicCheckedCallback checkedCallback;

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
        TopicEntity topicEntity = topics.get(position);
        ((TopicSelectViewHolder) holder).bindTo(topicEntity);
    }

    @Override
    public int getItemCount() {
        return topics == null ? 0 :  topics.size();
    }

    public void submitList(List<TopicEntity> topics) {
        this.topics = topics;
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
}
