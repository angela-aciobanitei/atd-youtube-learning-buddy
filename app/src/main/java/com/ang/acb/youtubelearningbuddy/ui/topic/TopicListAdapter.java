package com.ang.acb.youtubelearningbuddy.ui.topic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.databinding.TopicItemBinding;
import com.ang.acb.youtubelearningbuddy.utils.UiUtils;

import java.util.Date;
import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TopicEntity> topics;
    private TopicClickCallback clickCallback;

    TopicListAdapter(TopicClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout and get an instance of the binding class.
        TopicItemBinding itemBinding = TopicItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),  parent, false);
        return new TopicViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind item data
        TopicEntity topicEntity = topics.get(position);
        ((TopicViewHolder) holder).bindTo(topicEntity);

        // Handle item click events
        holder.itemView.setOnClickListener(v -> {
            if (topicEntity != null && clickCallback != null) {
                clickCallback.onClick(topicEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics == null ? 0 :  topics.size();
    }

    public TopicEntity getTopicAtPosition (int position) {
        return topics.get(position);
    }

    public void submitList(List<TopicEntity> topics) {
        this.topics = topics;
        // Notify any registered observers that the data set has changed.
        notifyDataSetChanged();
    }

    public interface TopicClickCallback {
        void onClick(TopicEntity topicEntity);
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {

        private TopicItemBinding binding;

        // Required constructor matching super
        TopicViewHolder(@NonNull TopicItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(TopicEntity topicEntity) {
            // Bind data for this item.
            binding.setTopic(topicEntity);

            binding.topicItemCreatedAt.setText(
                    UiUtils.dateToString(topicEntity.getCreatedAt()));

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}