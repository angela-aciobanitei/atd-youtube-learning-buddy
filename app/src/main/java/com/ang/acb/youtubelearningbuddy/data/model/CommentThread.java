package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A commentThread resource contains information about a YouTube comment thread,
 * which comprises a top-level comment and replies, if any exist, to that comment.
 * A commentThread resource can represent comments about either a video or a channel.
 * Both the top-level comment and the replies are actually comment resources nested
 * inside the commentThread resource. The commentThread resource does not necessarily
 * contain all replies to a comment, and you need to use the comments.list method if
 * you want to retrieve all replies for a particular comment.
 * See: https://developers.google.com/youtube/v3/docs/commentThreads
 *
 * The JSON structure below shows the format of a commentThreads resource:
 *
 * {
 *   "kind": "youtube#commentThread",
 *   "etag": etag,
 *   "id": string,
 *   "snippet": {
 *     "channelId": string,
 *     "videoId": string,
 *     "topLevelComment": comments Resource,
 *     "canReply": boolean,
 *     "totalReplyCount": unsigned integer,
 *     "isPublic": boolean
 *   },
 *   "replies": {
 *     "comments": [
 *       comments Resource
 *     ]
 *   }
 * }
 */
public class CommentThread {

    @SerializedName("id")
    @Expose
    private String commentThreadId;

    @SerializedName("snippet")
    @Expose
    private CommentThreadSnippet commentThreadSnippet;

    public String getCommentThreadId() {
        return commentThreadId;
    }

    public void setCommentThreadId(String commentThreadId) {
        this.commentThreadId = commentThreadId;
    }

    public CommentThreadSnippet getCommentThreadSnippet() {
        return commentThreadSnippet;
    }

    public void setCommentThreadSnippet(CommentThreadSnippet commentThreadSnippet) {
        this.commentThreadSnippet = commentThreadSnippet;
    }
}
