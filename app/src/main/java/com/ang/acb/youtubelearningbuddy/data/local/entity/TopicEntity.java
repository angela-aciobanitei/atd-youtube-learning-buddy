package com.ang.acb.youtubelearningbuddy.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Immutable model class for a video topic.
 */
@Entity(tableName = "topic")
public class TopicEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "topic_name")
    private String topicName;
}
