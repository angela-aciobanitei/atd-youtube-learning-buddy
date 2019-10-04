package com.ang.acb.youtubelearningbuddy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ang.acb.youtubelearningbuddy.data.local.entity.SearchEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.model.SearchResult;
import com.ang.acb.youtubelearningbuddy.data.model.SearchResultSnippet;
import com.ang.acb.youtubelearningbuddy.data.model.SearchVideosResponse;

import java.util.List;

/**
 * Interface for database access on {@link VideoEntity} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public abstract class  VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertVideo(VideoEntity video);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertVideos(List<VideoEntity> videos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSearchVideosResult(SearchEntity result);

    public void insertVideosFromResponse(SearchVideosResponse response) {
        List<SearchResult> searchResults = response.getSearchResults();
        for (SearchResult searchResult : searchResults) {
            SearchResultSnippet snippet = searchResult.getSearchResultSnippet();
            String videoId = searchResult.getSearchResultId().getVideoId();
            String publishedAt = snippet.getVideoPublishedAt();
            String title = snippet.getVideoTitle();
            String description = snippet.getVideoDescription();
            String thumbnailUrl = snippet.getVideoThumbnails().getHighResolutionVersion().getUrl();

            VideoEntity videoEntity = new VideoEntity(videoId, publishedAt, title, description, thumbnailUrl);
            insertVideo(videoEntity);
        }
    }

    @Query("SELECT * FROM search_results WHERE query = :query")
    public abstract LiveData<SearchEntity> search(String query);

    @Query("SELECT * FROM search_results WHERE query = :query")
    public abstract SearchEntity findSearchResult(String query);

    @Query("SELECT * FROM video WHERE id = :id")
    public abstract LiveData<VideoEntity> getVideoByRoomId(long id);

    @Query("SELECT * FROM video WHERE youtube_video_id = :youtubeId")
    public abstract LiveData<VideoEntity> getVideoByYouTubeId(String youtubeId);

    @Query("SELECT * FROM video WHERE youtube_video_id in (:youTubeVideoIds)")
    public abstract LiveData<List<VideoEntity>> loadVideosByIds(List<String> youTubeVideoIds);

}
