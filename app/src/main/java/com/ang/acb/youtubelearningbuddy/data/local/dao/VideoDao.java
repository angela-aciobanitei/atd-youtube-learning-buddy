package com.ang.acb.youtubelearningbuddy.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;

import java.util.List;

/**
 * Interface for database access on {@link VideoEntity} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideos(List<VideoEntity> videos);
}
