package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideosPageInfo {

    @SerializedName("totalResults")
    @Expose
    private int searchVideosTotalResults;

    @SerializedName("resultsPerPage")
    @Expose
    private int searchVideosResultsPerPage;

    public int getSearchVideosTotalResults() {
        return searchVideosTotalResults;
    }

    public void setSearchVideosTotalResults(int searchVideosTotalResults) {
        this.searchVideosTotalResults = searchVideosTotalResults;
    }

    public int getSearchVideosResultsPerPage() {
        return searchVideosResultsPerPage;
    }

    public void setSearchVideosResultsPerPage(int searchVideosResultsPerPage) {
        this.searchVideosResultsPerPage = searchVideosResultsPerPage;
    }
}
