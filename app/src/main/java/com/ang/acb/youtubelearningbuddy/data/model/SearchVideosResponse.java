package com.ang.acb.youtubelearningbuddy.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A search result contains information about a YouTube video, channel, or playlist
 * that matches the search parameters specified in an API request. While a search result
 * points to a uniquely identifiable resource, like a video, it does not have its own
 * persistent data. See: https://developers.google.com/youtube/v3/docs/search
 *
 * The API supports the following methods for search: list. It returns a collection of
 * search results that match the query parameters specified in the API request. By default,
 * a search result set identifies matching video, channel, and playlist resources, but you
 * can also configure queries to only retrieve a specific type of resource.
 * If successful, this method returns a response body with the following structure:
 *
 * {
 *   "kind": "youtube#searchListResponse",
 *   "etag": etag,
 *   "nextPageToken": string,
 *   "prevPageToken": string,
 *   "regionCode": string,
 *   "pageInfo": {
 *     "totalResults": integer,
 *     "resultsPerPage": integer
 *   },
 *   "items": [
 *     search Resource
 *   ]
 * }
 */
public class SearchVideosResponse {

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
    private List<SearchResult> searchResults;

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

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @NonNull
    public List<String> getVideoIds() {
        List<String> videoIds = new ArrayList<>();
        for (SearchResult searchResult : searchResults) {
            videoIds.add(searchResult.getSearchResultId().getVideoId());
        }
        return videoIds;
    }

    public int getTotalResults() {
        return searchResultPageInfo.getSearchVideosTotalResults();
    }


}
