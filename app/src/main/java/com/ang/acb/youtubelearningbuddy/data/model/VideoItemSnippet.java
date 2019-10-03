package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoItemSnippet {

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
    private VideoThumbnails videoThumbnails;




}
