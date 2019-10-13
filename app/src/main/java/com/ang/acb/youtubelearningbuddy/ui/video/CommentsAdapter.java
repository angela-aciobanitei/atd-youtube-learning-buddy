package com.ang.acb.youtubelearningbuddy.ui.video;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.youtubelearningbuddy.data.local.entity.CommentEntity;
import com.ang.acb.youtubelearningbuddy.databinding.CommentItemBinding;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentEntity> comments;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout and get an instance of the binding class.
        CommentItemBinding videoItemBinding = CommentItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent,false);
        return new CommentViewHolder(videoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommentViewHolder) holder).bindTo(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 :  comments.size();
    }

    public void updateData(List<CommentEntity> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        private CommentItemBinding binding;

        // Required constructor matching super.
        CommentViewHolder(@NonNull CommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(CommentEntity commentEntity) {
            // Bind data for this item.
            binding.setComment(commentEntity);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}
