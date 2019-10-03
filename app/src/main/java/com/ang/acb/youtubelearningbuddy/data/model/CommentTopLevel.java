package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class CommentTopLevel {

    @SerializedName("id")
    @Expose
    private String topLevelCommentId;

    @SerializedName("snippet")
    @Expose
    private CommentTopLevelSnippet topLevelCommentSnippet;

    public String getTopLevelCommentId() {
        return topLevelCommentId;
    }

    public void setTopLevelCommentId(String topLevelCommentId) {
        this.topLevelCommentId = topLevelCommentId;
    }

    public CommentTopLevelSnippet getTopLevelCommentSnippet() {
        return topLevelCommentSnippet;
    }

    public void setTopLevelCommentSnippet(CommentTopLevelSnippet topLevelCommentSnippet) {
        this.topLevelCommentSnippet = topLevelCommentSnippet;
    }
}
