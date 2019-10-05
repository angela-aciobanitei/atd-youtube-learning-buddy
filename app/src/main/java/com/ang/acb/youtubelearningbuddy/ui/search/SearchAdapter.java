package com.ang.acb.youtubelearningbuddy.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.databinding.VideoItemBinding;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoEntity> videos;
    private VideoClickCallback clickCallback;

    SearchAdapter(VideoClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout and get an instance of the binding class.
        VideoItemBinding videoItemBinding = VideoItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new SearchViewHolder(videoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind item data
        VideoEntity videoEntity = videos.get(position);
        ((SearchViewHolder) holder).bindTo(videoEntity);

        // Handle item click events
        holder.itemView.setOnClickListener(v -> {
            if (videoEntity != null && clickCallback != null) {
                clickCallback.onClick(videoEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 :  videos.size();
    }

    public void submitList(List<VideoEntity> videos) {
        this.videos = videos;
        // Notify any registered observers
        // that the data set has changed.
        notifyDataSetChanged();
    }

    public interface VideoClickCallback {
        void onClick(VideoEntity videoEntity);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private VideoItemBinding binding;

        // Required constructor matching super
        SearchViewHolder(@NonNull VideoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        void bindTo(VideoEntity videoEntity) {
            // Bind data for this item.
            binding.setVideo(videoEntity);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}
