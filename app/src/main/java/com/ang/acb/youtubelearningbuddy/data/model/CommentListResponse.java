package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A comment resource contains information about a single YouTube comment. A
 * comment resource can represent a comment about either a video or a channel.
 * The comment could be a top-level comment or a reply to a top-level comment.
 * See: https://developers.google.com/youtube/v3/docs/comments
 *
 * The list method returns a list of comments that match the API request parameters.
 * See: https://developers.google.com/youtube/v3/docs/comments/list#response_1
 * If successful, this method returns a response body with the following structure:
 *
 * {
 *   "kind": "youtube#commentListResponse",
 *   "etag": etag,
 *   "nextPageToken": string,
 *   "pageInfo": {
 *     "totalResults": integer,
 *     "resultsPerPage": integer
 *   },
 *   "items": [
 *     comment Resource
 *   ]
 * }
 */
public class CommentListResponse {

    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;

    @SerializedName("pageInfo")
    @Expose
    private CommentsPageInfo commentsPageInfo;

    @SerializedName("items")
    @Expose
    private List<CommentResource> commentItems;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public CommentsPageInfo getCommentsPageInfo() {
        return commentsPageInfo;
    }

    public void setCommentsPageInfo(CommentsPageInfo commentsPageInfo) {
        this.commentsPageInfo = commentsPageInfo;
    }

    public List<CommentResource> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(List<CommentResource> commentItems) {
        this.commentItems = commentItems;
    }
}
