package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResultSnippet {

    // The date and time when the resource was originally published.
    // The value is specified in ISO 8601 (YYYY-MM-DDThh:mm:ss.sZ) format.
    // See: https://developers.google.com/youtube/v3/docs/search#properties_1
    @SerializedName("publishedAt")
    @Expose
    private String videoPublishedAt;

    @SerializedName("title")
    @Expose
    private String videoTitle;

    @SerializedName("description")
    @Expose
    private String videoDescription;

    @SerializedName("thumbnails")
    @Expose
    private ThumbnailsResource videoThumbnails;


    public String getVideoPublishedAt() {
        return videoPublishedAt;
    }

    public void setVideoPublishedAt(String videoPublishedAt) {
        this.videoPublishedAt = videoPublishedAt;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public ThumbnailsResource getVideoThumbnails() {
        return videoThumbnails;
    }

    public void setVideoThumbnails(ThumbnailsResource videoThumbnails) {
        this.videoThumbnails = videoThumbnails;
    }
}
