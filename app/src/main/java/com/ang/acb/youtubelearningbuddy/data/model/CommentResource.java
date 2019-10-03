package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A comment resource contains information about a single YouTube comment. A
 * comment resource can represent a comment about either a video or a channel.
 * The comment could be a top-level comment or a reply to a top-level comment.
 * The following JSON structure shows the format of a comments resource:
 * See: https://developers.google.com/youtube/v3/docs/comments#resource-representation
 *
 * {
 *   "kind": "youtube#comment",
 *   "etag": etag,
 *   "id": string,
 *   "snippet": {
 *     "authorDisplayName": string,
 *     "authorProfileImageUrl": string,
 *     "authorChannelUrl": string,
 *     "authorChannelId": {
 *       "value": string
 *     },
 *     "channelId": string,
 *     "videoId": string,
 *     "textDisplay": string,
 *     "textOriginal": string,
 *     "parentId": string,
 *     "canRate": boolean,
 *     "viewerRating": string,
 *     "likeCount": unsigned integer,
 *     "moderationStatus": string,
 *     "publishedAt": datetime,
 *     "updatedAt": datetime
 *   }
 * }
 */
public class CommentResource {

    @SerializedName("id")
    @Expose
    private String commentResourceId;

    @SerializedName("snippet")
    @Expose
    private CommentResourceSnippet commentResourceSnippet;

    public String getCommentResourceId() {
        return commentResourceId;
    }

    public void setCommentResourceId(String commentResourceId) {
        this.commentResourceId = commentResourceId;
    }

    public CommentResourceSnippet getCommentResourceSnippet() {
        return commentResourceSnippet;
    }

    public void setCommentResourceSnippet(CommentResourceSnippet commentResourceSnippet) {
        this.commentResourceSnippet = commentResourceSnippet;
    }
}
