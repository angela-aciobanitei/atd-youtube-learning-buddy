package com.ang.acb.youtubelearningbuddy.ui.video;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideoItemViewHolder> {

    private List<VideoEntity> videos;
    private VideoItemViewHolder.VideoClickCallback clickCallback;

    public VideosAdapter(VideoItemViewHolder.VideoClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public VideoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VideoItemViewHolder.createViewHolder(parent, clickCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemViewHolder holder, int position) {
        holder.bindTo(videos.get(position));

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
}
