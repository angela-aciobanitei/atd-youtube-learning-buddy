package com.ang.acb.youtubelearningbuddy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.youtubelearningbuddy.data.local.entity.SearchEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.model.SearchResult;
import com.ang.acb.youtubelearningbuddy.data.model.SearchResultSnippet;
import com.ang.acb.youtubelearningbuddy.data.model.SearchVideosResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for database access on {@link VideoEntity} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public abstract class  VideoDao {

    // Note: If the @Insert method receives only 1 parameter, it can return a long,
    // which is the new rowId for the inserted item. If the parameter is an array or
    // a collection, it should return long[] or List<Long> instead.
    // See: https://developer.android.com/training/data-storage/room/accessing-data#convenience-insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertVideo(VideoEntity video);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertVideos(List<VideoEntity> videos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertSearchResult(SearchEntity result);

    public int insertVideosFromResponse(SearchVideosResponse response) {
        List<SearchResult> searchResults = response.getSearchResults();
        List<Long> videoIds = new ArrayList<>();
        for (SearchResult searchResult : searchResults) {
            VideoEntity videoEntity = getVideoFromSearchResult(searchResult);
            if (videoEntity != null) {
                videoIds.add(insertVideo(videoEntity));
            }
        }
        return videoIds.size();
    }

    private VideoEntity getVideoFromSearchResult(SearchResult searchResult) {
        String videoId = searchResult.getSearchResultId().getVideoId();
        VideoEntity videoEntity = null;
        if (videoId != null) {
            SearchResultSnippet snippet = searchResult.getSearchResultSnippet();
            String publishedAt = snippet.getVideoPublishedAt();
            String title = snippet.getVideoTitle();
            String description = snippet.getVideoDescription();
            String defaultThumbUrl = snippet.getVideoThumbnails().getDefaultResolutionVersion().getUrl();
            String highThumbUrl = snippet.getVideoThumbnails().getHighResolutionVersion().getUrl();

            videoEntity = new VideoEntity(videoId, publishedAt, title, description,
                                          defaultThumbUrl, highThumbUrl);
        }

        return videoEntity;
    }

    @Query("SELECT * FROM search_results WHERE `query` = :query")
    public abstract LiveData<SearchEntity> search(String query);

    @Query("SELECT * FROM search_results WHERE `query` = :query")
    public abstract SearchEntity findSearchResult(String query);

    @Query("SELECT * FROM video WHERE id = :id")
    public abstract LiveData<VideoEntity> getVideoByRoomId(long id);

    @Transaction
    @Query("SELECT * FROM video WHERE youtube_video_id = :youtubeId")
    public abstract LiveData<VideoEntity> getVideoByYouTubeId(String youtubeId);

    @Transaction
    @Query("SELECT id FROM video WHERE youtube_video_id = :youtubeId")
    public abstract long getVideoId(String youtubeId);

    @Transaction
    @Query("SELECT * FROM video WHERE youtube_video_id in (:youTubeVideoIds)")
    public abstract LiveData<List<VideoEntity>> loadVideosByIds(List<String> youTubeVideoIds);
}
