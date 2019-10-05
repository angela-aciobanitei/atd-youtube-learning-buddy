package com.ang.acb.youtubelearningbuddy.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import javax.inject.Inject;


/**
 * Immutable model class for a video. Note: Since a video can have zero or more
 * comments, we need to define a one-to-many relationship between these two entities.
 * Also, since a topic can have any number of videos, and a video can be included in
 * any number of topics, we need to define a many-to-many relationship between them.
 */
@Entity(tableName = "video")
public class VideoEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "youtube_video_id")
    private String youTubeVideoId;

    @ColumnInfo(name = "published_at")
    private String publishedAt;

    private String title;

    private String description;

    @ColumnInfo(name = "thumbnail_url")
    private String thumbnailUrl;

    public VideoEntity(long id, String youTubeVideoId, String publishedAt,
                       String title, String description, String thumbnailUrl) {
        this.id = id;
        this.youTubeVideoId = youTubeVideoId;
        this.publishedAt = publishedAt;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Ignore
    @Inject
    public VideoEntity(String youTubeVideoId, String publishedAt, String title,
                       String description, String thumbnailUrl) {
        this.youTubeVideoId = youTubeVideoId;
        this.publishedAt = publishedAt;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getYouTubeVideoId() {
        return youTubeVideoId;
    }

    public void setYouTubeVideoId(String youTubeVideoId) {
        this.youTubeVideoId = youTubeVideoId;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
