package com.ang.acb.youtubelearningbuddy.data.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
 * Its list method returns a list of comment threads that match the API request
 * parameters. See: https://developers.google.com/youtube/v3/docs/commentThreads/list
 * If successful, this method returns a response body with the following structure:
 *
 * {
 *   "kind": "youtube#commentThreadListResponse",
 *   "etag": etag,
 *   "nextPageToken": string,
 *   "pageInfo": {
 *     "totalResults": integer,
 *     "resultsPerPage": integer
 *   },
 *   "items": [
 *     commentThread Resource
 *   ]
 * }
 */
public class CommentThreadListResponse {

    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;

    @SerializedName("items")
    @Expose
    private List<CommentThread> commentThreads;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<CommentThread> getCommentThreads() {
        return commentThreads;
    }

    public void setCommentThreads(List<CommentThread> commentThreads) {
        this.commentThreads = commentThreads;
    }
}
