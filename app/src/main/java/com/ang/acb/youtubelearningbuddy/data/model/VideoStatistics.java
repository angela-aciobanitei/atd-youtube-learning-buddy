package com.ang.acb.youtubelearningbuddy.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoStatistics {

    @SerializedName("viewCount")
    @Expose
    private long viewCount;

    @SerializedName("likeCount")
    @Expose
    private long likeCount;

    @SerializedName("dislikeCount")
    @Expose
    private long dislikeCount;

    @SerializedName("commentCount")
    @Expose
    private long commentCount;

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(long dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }
}
