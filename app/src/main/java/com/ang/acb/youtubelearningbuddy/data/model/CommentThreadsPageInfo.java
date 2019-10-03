package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentThreadsPageInfo {

    @SerializedName("totalResults")
    @Expose
    private int commentsTotalResults;

    @SerializedName("resultsPerPage")
    @Expose
    private int commentsResultsPerPage;

    public int getCommentsTotalResults() {
        return commentsTotalResults;
    }

    public void setCommentsTotalResults(int commentsTotalResults) {
        this.commentsTotalResults = commentsTotalResults;
    }

    public int getCommentsResultsPerPage() {
        return commentsResultsPerPage;
    }

    public void setCommentsResultsPerPage(int commentsResultsPerPage) {
        this.commentsResultsPerPage = commentsResultsPerPage;
    }
}
