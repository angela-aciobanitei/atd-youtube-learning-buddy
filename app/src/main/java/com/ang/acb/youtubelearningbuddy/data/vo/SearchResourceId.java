package com.ang.acb.youtubelearningbuddy.data.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResourceId {

    @SerializedName("videoId")
    @Expose
    private String videoId;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
