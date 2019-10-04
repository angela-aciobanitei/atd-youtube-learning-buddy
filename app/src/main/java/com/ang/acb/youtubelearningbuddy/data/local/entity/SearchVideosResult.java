package com.ang.acb.youtubelearningbuddy.data.local.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.List;

@Entity(primaryKeys = {"query"})
public class SearchVideosResult {

    @NonNull
    public final String query;
    public final List<String> youTubeVideoIds;
    public final int totalResults;
    @Nullable
    public final String nextPageToken;

    public SearchVideosResult(@NonNull String query, List<String> youTubeVideoIds,
                              int totalResults, @Nullable String nextPageToken) {
        this.query = query;
        this.youTubeVideoIds = youTubeVideoIds;
        this.totalResults = totalResults;
        this.nextPageToken = nextPageToken;
    }
}
