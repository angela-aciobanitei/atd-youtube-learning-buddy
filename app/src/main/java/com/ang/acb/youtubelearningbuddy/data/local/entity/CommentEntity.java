package com.ang.acb.youtubelearningbuddy.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Immutable model class for a comment. Since zero or more instances of CommentEntity can
 * be linked to a single instance of VideoEntity through the "video_id" foreign key, this
 * models a one-to-many relationship between CommentEntity and VideoEntity.
 *
 * See: https://developer.android.com/training/data-storage/room/relationships#one-to-many
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
@Entity(tableName = "comment",
        foreignKeys = @ForeignKey(entity = VideoEntity.class,
                                  parentColumns = "id",
                                  childColumns = "room_video_id",
                                  onDelete = CASCADE,
                                  onUpdate = CASCADE),
        indices = {@Index(value = {"room_video_id"})}
)
public class CommentEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "room_video_id")
    private long roomVideoId;

    @ColumnInfo(name = "youtube_video_id")
    private String youTubeVideoId;

    @ColumnInfo(name = "author_display_name")
    private String authorDisplayName;

    @ColumnInfo(name = "author_profile_image_url")
    private String authorProfileImageUrl;

    @ColumnInfo(name = "display_text")
    private String displayText;

    @ColumnInfo(name = "published_at")
    private String publishedAt;

    public CommentEntity(long id, long roomVideoId, String youTubeVideoId, String authorDisplayName,
                         String authorProfileImageUrl, String displayText, String publishedAt) {
        this.id = id;
        this.roomVideoId = roomVideoId;
        this.youTubeVideoId = youTubeVideoId;
        this.authorDisplayName = authorDisplayName;
        this.authorProfileImageUrl = authorProfileImageUrl;
        this.displayText = displayText;
    }

    @Ignore
    public CommentEntity(long roomVideoId, String youTubeVideoId, String authorDisplayName,
                         String authorProfileImageUrl, String displayText, String publishedAt) {
        this.roomVideoId = roomVideoId;
        this.youTubeVideoId = youTubeVideoId;
        this.authorDisplayName = authorDisplayName;
        this.authorProfileImageUrl = authorProfileImageUrl;
        this.displayText = displayText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoomVideoId() {
        return roomVideoId;
    }

    public void setRoomVideoId(long roomVideoId) {
        this.roomVideoId = roomVideoId;
    }

    public String getYouTubeVideoId() {
        return youTubeVideoId;
    }

    public void setYouTubeVideoId(String youTubeVideoId) {
        this.youTubeVideoId = youTubeVideoId;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public String getAuthorProfileImageUrl() {
        return authorProfileImageUrl;
    }

    public void setAuthorProfileImageUrl(String authorProfileImageUrl) {
        this.authorProfileImageUrl = authorProfileImageUrl;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
