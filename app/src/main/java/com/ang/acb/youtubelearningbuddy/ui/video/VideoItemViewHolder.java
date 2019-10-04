package com.ang.acb.youtubelearningbuddy.ui.video;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.databinding.VideoItemBinding;

public class VideoItemViewHolder extends RecyclerView.ViewHolder {

    private VideoItemBinding binding;
    private VideoClickCallback clickCallback;

    // Required constructor matching super
    private VideoItemViewHolder(@NonNull VideoItemBinding binding, VideoClickCallback clickCallback) {
        super(binding.getRoot());

        this.binding = binding;
        this.clickCallback = clickCallback;
    }

    static VideoItemViewHolder createViewHolder(ViewGroup parent, VideoClickCallback clickCallback) {
        // Inflate view and obtain an instance of the binding class.
        VideoItemBinding binding = VideoItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new VideoItemViewHolder(binding, clickCallback);
    }


    void bindTo(VideoEntity videoEntity) {
        // Bind article data (title, subtitle and thumbnail)
        binding.setVideo(videoEntity);

        // Handle article items click events.
        binding.getRoot().setOnClickListener(rootView -> {
            clickCallback.onClick(videoEntity);
        });

        // Binding must be executed immediately.
        binding.executePendingBindings();
    }

    public interface VideoClickCallback {
        void onClick(VideoEntity videoEntity);
    }
}
