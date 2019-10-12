package com.ang.acb.youtubelearningbuddy.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;

/**
 * Since each topic can have any number of videos, and each video can be included in any
 * number of topics, we need to define a many-to-many relationship between two entities,
 * where each entity can be linked to zero or more instances of the other.
 *
 * See: https://developer.android.com/training/data-storage/room/relationships#many-to-many
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
@Entity(tableName = "video_topic_join",
        primaryKeys = { "topicId", "videoId" },
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "topicId"),
                @ForeignKey(entity = VideoEntity.class,
                        parentColumns = "id",
                        childColumns = "videoId")
        }
)
public class VideoTopicJoin {

    // Create index to cover this column to handle this Room warning:
    // "Warning: topicId column references a foreign key but it is not part of an index.
    // This may trigger full table scans whenever parent table is modified so you are
    // highly advised to create an index that covers this column."
    // See: https://stackoverflow.com/questions/44480761/android-room-compile-time-warning-about-column-in-foreign-key-not-part-of-an-ind
    @ColumnInfo(index = true)
    private long topicId;

    @ColumnInfo(index = true)
    private long videoId;

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }
}
