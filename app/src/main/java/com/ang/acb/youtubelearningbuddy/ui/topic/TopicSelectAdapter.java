package com.ang.acb.youtubelearningbuddy.ui.topic;


import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.databinding.TopicSelectItemBinding;
import com.ang.acb.youtubelearningbuddy.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class TopicSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Keeps track of all existing topics.
    private List<TopicEntity> topicEntities = new ArrayList<>();

    // Keeps track of which topic is checked/unchecked.
    private LongSparseArray<Boolean> topicStates = new LongSparseArray<>();

    private SelectTopicCallback checkedCallback;

    TopicSelectAdapter(SelectTopicCallback checkedCallback) {
        this.checkedCallback = checkedCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout and get an instance of the binding class.
        TopicSelectItemBinding itemBinding = TopicSelectItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TopicSelectViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TopicSelectViewHolder) holder).bindTo(topicEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return topicEntities == null ? 0 :  topicEntities.size();
    }

    public void updateData(List<TopicEntity> topicEntities, LongSparseArray<Boolean> topicStates) {
        this.topicEntities = topicEntities;
        this.topicStates = topicStates;
        // Notify any registered observers that the data set has changed.
        notifyDataSetChanged();
    }

    public interface SelectTopicCallback {
        void onTopicChecked(TopicEntity topicEntity);
        void onTopicUnchecked(TopicEntity topicEntity);
    }

    class TopicSelectViewHolder extends RecyclerView.ViewHolder {

        private TopicSelectItemBinding binding;

        // Required constructor matching super.
        TopicSelectViewHolder(@NonNull TopicSelectItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(TopicEntity topicEntity) {
            // Bind data for this item.
            binding.setTopic(topicEntity);
            binding.selectTopicCreatedAt.setText(
                    UiUtils.dateToString(topicEntity.getCreatedAt()));

            // Remove the previous OnCheckedChangeListener.
            binding.selectTopicCheckbox.setOnCheckedChangeListener(null);

            // Get checkbox state from LongSparseArray topicStates.
            binding.selectTopicCheckbox.setChecked(topicStates.get(topicEntity.getId()));

            // Register a callback to be invoked when the checked state of this button changes.
            binding.selectTopicCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (checkedCallback != null) {
                    if (isChecked) checkedCallback.onTopicChecked(topicEntity);
                    else checkedCallback.onTopicUnchecked(topicEntity);
                }
            });

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}
