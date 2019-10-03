package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A search result contains information about a YouTube video, channel, or playlist
 * that matches the search parameters specified in an API request. While a search result
 * points to a uniquely identifiable resource, like a video, it does not have its own
 * persistent data. See: https://developers.google.com/youtube/v3/docs/search
 * The following JSON structure shows the format of a search result:
 *
 * {
 *   "kind": "youtube#searchResult",
 *   "etag": etag,
 *   "id": {
 *     "kind": string,
 *     "videoId": string,
 *     "channelId": string,
 *     "playlistId": string
 *   },
 *   "snippet": {
 *     "publishedAt": datetime,
 *     "channelId": string,
 *     "title": string,
 *     "description": string,
 *     "thumbnails": {
 *       (key): {
 *         "url": string,
 *         "width": unsigned integer,
 *         "height": unsigned integer
 *       }
 *     },
 *     "channelTitle": string,
 *     "liveBroadcastContent": string
 *   }
 * }
 */
public class VideoItem {

    @SerializedName("id")
    @Expose
    private VideoItemId videoItemId;

    @SerializedName("snippet")
    @Expose
    private VideoItemSnippet videoItemSnippet;

    public VideoItemId getVideoItemId() {
        return videoItemId;
    }

    public void setVideoItemId(VideoItemId videoItemId) {
        this.videoItemId = videoItemId;
    }

    public VideoItemSnippet getVideoItemSnippet() {
        return videoItemSnippet;
    }

    public void setVideoItemSnippet(VideoItemSnippet videoItemSnippet) {
        this.videoItemSnippet = videoItemSnippet;
    }
}
