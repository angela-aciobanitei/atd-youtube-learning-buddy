package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The HTTP request GET https://www.googleapis.com/youtube/v3/videos returns list
 * of videos that match the API request parameters.
 * See: https://developers.google.com/youtube/v3/docs/videos/list#response
 *
 * If successful, this method returns a response body with the following structure:
 *
 * {
 *   "kind": "youtube#videoListResponse",
 *   "etag": etag,
 *   "nextPageToken": string,
 *   "prevPageToken": string,
 *   "pageInfo": {
 *     "totalResults": integer,
 *     "resultsPerPage": integer
 *   },
 *   "items": [
 *     video Resource
 *   ]
 * }
 */
public class VideoListResponse {

    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;

    @SerializedName("prevPageToken")
    @Expose
    private String prevPageToken;

    @SerializedName("pageInfo")
    @Expose
    private SearchResultPageInfo searchResultPageInfo;

    @SerializedName("items")
    @Expose
    private List<VideoResource> videoResources;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }

    public SearchResultPageInfo getSearchResultPageInfo() {
        return searchResultPageInfo;
    }

    public void setSearchResultPageInfo(SearchResultPageInfo searchResultPageInfo) {
        this.searchResultPageInfo = searchResultPageInfo;
    }

    public List<VideoResource> getVideoResources() {
        return videoResources;
    }

    public void setVideoResources(List<VideoResource> videoResources) {
        this.videoResources = videoResources;
    }
}
