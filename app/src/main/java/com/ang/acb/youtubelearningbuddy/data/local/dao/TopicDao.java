package com.ang.acb.youtubelearningbuddy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;

import java.util.List;

/**
 * Interface for database access on {@link TopicEntity} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTopic(TopicEntity topic);

    @Delete
    void delete(TopicEntity topicEntity);

    @Query("DELETE FROM topic WHERE id = :id")
    void deleteTopicById(long id);

    @Transaction
    @Query("SELECT * FROM topic WHERE id = :id")
    LiveData<TopicEntity> getTopicById(long id);

    @Transaction
    @Query("SELECT name FROM topic")
    LiveData<List<String>> getAllTopicNames();

    @Transaction
    @Query("SELECT * FROM topic")
    LiveData<List<TopicEntity>> getAllTopics();
}
