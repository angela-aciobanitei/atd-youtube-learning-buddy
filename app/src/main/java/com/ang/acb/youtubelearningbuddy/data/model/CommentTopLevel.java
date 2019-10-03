package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A comment resource contains information about a single YouTube comment. A comment
 * resource can represent a comment about either a video or a channel. In addition,
 * the comment could be a top-level comment or a reply to a top-level comment.
 * See: https://developers.google.com/youtube/v3/docs/comments#resource
 *
 * The following JSON structure shows the format of a comments resource:
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
class CommentTopLevel {

    @SerializedName("id")
    @Expose
    private String topLevelCommentId;

    @SerializedName("snippet")
    @Expose
    private CommentTopLevelSnippet topLevelCommentSnippet;

    @SerializedName("videoId")
    @Expose
    private SearchResultId videoId;

    @SerializedName("textDisplay")
    @Expose
    private String textDisplay;

    public String getTopLevelCommentId() {
        return topLevelCommentId;
    }

    public void setTopLevelCommentId(String topLevelCommentId) {
        this.topLevelCommentId = topLevelCommentId;
    }

    public CommentTopLevelSnippet getTopLevelCommentSnippet() {
        return topLevelCommentSnippet;
    }

    public void setTopLevelCommentSnippet(CommentTopLevelSnippet topLevelCommentSnippet) {
        this.topLevelCommentSnippet = topLevelCommentSnippet;
    }


}
