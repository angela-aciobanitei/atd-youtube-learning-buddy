package com.ang.acb.youtubelearningbuddy.ui.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ang.acb.youtubelearningbuddy.R;

/**
 * Project Spec
 * ============
 * 1. Create the app with the ability to search for videos using the YouTube API.
 * 2. When the results are returned, allow users add videos to user-specified topic areas.
 * 3. Include a description of the video being played in the interface.
 * 4. After a topic is selected, user is able to watch those videos from inside the app.
 * 5. Streaming videos from YouTube are handled with a media intent to an existing media player.
 * 6. Extra: Allow users to add/remove playlists to topics.
 * 7. Extra: Show video ratings alongside each video.
 * 8. Extra: Play videos directly in the app using the YouTubePlayerView.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
