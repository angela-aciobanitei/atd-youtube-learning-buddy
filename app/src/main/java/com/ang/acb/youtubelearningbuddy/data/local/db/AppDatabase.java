package com.ang.acb.youtubelearningbuddy.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ang.acb.youtubelearningbuddy.data.local.dao.CommentDao;
import com.ang.acb.youtubelearningbuddy.data.local.dao.TopicDao;
import com.ang.acb.youtubelearningbuddy.data.local.dao.VideoDao;
import com.ang.acb.youtubelearningbuddy.data.local.dao.VideoTopicJoinDao;
import com.ang.acb.youtubelearningbuddy.data.local.entity.CommentEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.SearchEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.TopicEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoEntity;
import com.ang.acb.youtubelearningbuddy.data.local.entity.VideoTopicJoin;

/**
 * The Room database for this app.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Database(entities = {SearchEntity.class,
                      CommentEntity.class,
                      TopicEntity.class,
                      VideoEntity.class,
                      VideoTopicJoin.class},
          version = 15,
          exportSchema = false)
@TypeConverters({StringConverter.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract CommentDao commentDao();
    public abstract TopicDao topicDao();
    public abstract VideoDao videoDao();
    public abstract VideoTopicJoinDao videoTopicJoinDao();
}
