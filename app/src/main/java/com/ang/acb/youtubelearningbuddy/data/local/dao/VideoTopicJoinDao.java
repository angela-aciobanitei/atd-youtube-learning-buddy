package com.ang.acb.youtubelearningbuddy.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoTopicJoin;
import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;

import java.util.List;

/**
 * Since each topic can have any number of videos, and each video can be included in any
 * number of topics, we need to define a many-to-many relationship between two entities,
 * where each entity can be linked to zero or more instances of the other.
 *
 * See: https://developer.android.com/training/data-storage/room/relationships#many-to-many
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
@Dao
public interface VideoTopicJoinDao {

    @Insert
    void insert(VideoTopicJoin videoTopicJoin);

    @Query("SELECT id, topic_name FROM topic " +
            "INNER JOIN video_topic_join " +
            "ON topic.id=video_topic_join.topicId " +
            "WHERE video_topic_join.videoId=:videoId")
    List<TopicEntity> getTopicsForVideo(final long videoId);

    @Query("SELECT id, youtube_video_id, published_at, title, description, default_thumbnail_url, high_thumbnail_url FROM video " +
            "INNER JOIN video_topic_join " +
            "ON video.id=video_topic_join.videoId " +
            "WHERE video_topic_join.topicId=:topicId")
    List<VideoEntity> getVideosForTopic(final long topicId);
}
