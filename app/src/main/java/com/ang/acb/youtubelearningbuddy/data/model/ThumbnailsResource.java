package com.ang.acb.youtubelearningbuddy.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A thumbnail resource identifies different thumbnail image sizes associated with a resource.
 * The following JSON structure shows the format of a thumbnails resource:
 *
 * {
 *   "default": {
 *     "url": string,
 *     "width": unsigned integer,
 *     "height": unsigned integer
 *   },
 *   "medium": {
 *     "url": string,
 *     "width": unsigned integer,
 *     "height": unsigned integer
 *   },
 *   "high": {
 *     "url": string,
 *     "width": unsigned integer,
 *     "height": unsigned integer
 *   },
 *   "standard": {
 *     "url": string,
 *     "width": unsigned integer,
 *     "height": unsigned integer
 *   },
 *   "maxres": {
 *
 * See: https://developers.google.com/youtube/v3/docs/thumbnails
 */
public class ThumbnailsResource {

    @SerializedName("high")
    @Expose
    private ThumbnailsHigh highResolutionVersion;

    @SerializedName("default")
    @Expose
    private ThumbnailsHigh defaultResolutionVersion;


    public ThumbnailsHigh getDefaultResolutionVersion() {
        return defaultResolutionVersion;
    }

    public void setDefaultResolutionVersion(ThumbnailsHigh defaultResolutionVersion) {
        this.defaultResolutionVersion = defaultResolutionVersion;
    }

    public ThumbnailsHigh getHighResolutionVersion() {
        return highResolutionVersion;
    }

    public void setHighResolutionVersion(ThumbnailsHigh highResolutionVersion) {
        this.highResolutionVersion = highResolutionVersion;
    }
}
