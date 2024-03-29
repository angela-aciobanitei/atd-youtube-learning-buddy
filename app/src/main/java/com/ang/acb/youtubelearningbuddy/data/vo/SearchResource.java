package com.ang.acb.youtubelearningbuddy.data.vo;

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
public class SearchResource {

    @SerializedName("id")
    @Expose
    private SearchResourceId searchResourceId;

    @SerializedName("snippet")
    @Expose
    private SearchResourceSnippet searchResourceSnippet;

    public SearchResourceId getSearchResourceId() {
        return searchResourceId;
    }

    public void setSearchResourceId(SearchResourceId searchResourceId) {
        this.searchResourceId = searchResourceId;
    }

    public SearchResourceSnippet getSearchResourceSnippet() {
        return searchResourceSnippet;
    }

    public void setSearchResourceSnippet(SearchResourceSnippet searchResourceSnippet) {
        this.searchResourceSnippet = searchResourceSnippet;
    }
}
