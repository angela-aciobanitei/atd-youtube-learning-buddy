package com.ang.acb.youtubelearningbuddy.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.databinding.VideoItemBinding;
import com.ang.acb.youtubelearningbuddy.utils.UiUtils;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoEntity> videos;
    private VideoClickCallback clickCallback;

    VideosAdapter(VideoClickCallback clickCallback) {
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
        return new VideoViewHolder(videoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind item data
        VideoEntity videoEntity = videos.get(position);
        ((VideoViewHolder) holder).bindTo(videoEntity);

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

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private VideoItemBinding binding;

        // Required constructor matching super
        VideoViewHolder(@NonNull VideoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        void bindTo(VideoEntity videoEntity) {
            // Bind data for this item.
            binding.setVideo(videoEntity);
            // Date is an RFC 3339 formatted date-time value (1970-01-01T00:00:00Z).
            binding.videoItemPublishedAt.setText(UiUtils.formatPublishedAtDate(
                    videoEntity.getPublishedAt()).toString());

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}
