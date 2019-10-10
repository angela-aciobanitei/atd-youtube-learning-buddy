package com.ang.acb.youtubelearningbuddy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.youtubelearningbuddy.data.local.entity.CommentEntity;
import com.ang.acb.youtubelearningbuddy.data.vo.CommentThread;
import com.ang.acb.youtubelearningbuddy.data.vo.CommentThreadListResponse;
import com.ang.acb.youtubelearningbuddy.data.vo.CommentThreadSnippet;
import com.ang.acb.youtubelearningbuddy.data.vo.CommentTopLevel;
import com.ang.acb.youtubelearningbuddy.data.vo.CommentTopLevelSnippet;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for database access on {@link CommentEntity} related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public abstract class CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertComment(CommentEntity commentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertComments(List<CommentEntity> comments);

    @Transaction
    @Query("SELECT * FROM comment WHERE id = :id")
    public abstract LiveData<CommentEntity> getCommentByRoomId(long id);

    @Transaction
    @Query("SELECT * FROM comment WHERE comment.youtube_video_id = :youtubeVideoId")
    public abstract LiveData<List<CommentEntity>> getCommentsForVideo(String youtubeVideoId);

    public int insertCommentsFromResponse(long roomVideoId, CommentThreadListResponse response) {
        List<CommentThread> commentThreads = response.getCommentThreads();
        List<Long> ids = new ArrayList<>();
        for (CommentThread commentThread : commentThreads) {
            CommentEntity commentEntity = getCommentFromResult(roomVideoId, commentThread);
            if (commentEntity != null) {
                ids.add(insertComment(commentEntity));
            }
        }

        return ids.size();
    }

    private CommentEntity getCommentFromResult(long roomVideoId, CommentThread commentThread) {
        String commentThreadId = commentThread.getCommentThreadId();
        CommentEntity commentEntity = null;
        if (commentThreadId != null) {
            CommentThreadSnippet threadSnippet = commentThread.getCommentThreadSnippet();
            String youtubeVideoId = threadSnippet.getVideoId();
            CommentTopLevel topLevelComment = threadSnippet.getTopLevelComment();
            CommentTopLevelSnippet commentSnippet = topLevelComment.getTopLevelCommentSnippet();
            String authorDisplayName = commentSnippet.getAuthorDisplayName();
            String authorProfileImageUrl = commentSnippet.getAuthorProfileImageUrl();
            String textDisplay = commentSnippet.getTextDisplay();
            String publishedAt = commentSnippet.getPublishedAt();

            commentEntity = new CommentEntity(roomVideoId, youtubeVideoId, authorDisplayName,
                                              authorProfileImageUrl, textDisplay, publishedAt);
        }

        return commentEntity;
    }
}
