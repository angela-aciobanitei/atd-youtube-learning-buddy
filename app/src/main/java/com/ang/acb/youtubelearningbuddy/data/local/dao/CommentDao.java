package com.ang.acb.youtubelearningbuddy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.youtubelearningbuddy.data.local.entity.CommentEntity;

import java.util.List;

/**
 * Interface for database access on {@link CommentEntity} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(CommentEntity commentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<CommentEntity> comments);

    @Transaction
    @Query("SELECT * FROM comment WHERE id = :id")
    LiveData<CommentEntity> getCommentById(long id);

    @Transaction
    @Query("SELECT * FROM comment WHERE comment.video_id = :videoId")
    LiveData<List<CommentEntity>> getCommentsForVideoId(long videoId);
}
