package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentThreadSnippet {

    @SerializedName("videoId")
    @Expose
    private String videoId;

    @SerializedName("topLevelComment")
    @Expose
    private CommentTopLevel topLevelComment;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public CommentTopLevel getTopLevelComment() {
        return topLevelComment;
    }

    public void setTopLevelComment(CommentTopLevel topLevelComment) {
        this.topLevelComment = topLevelComment;
    }
}
