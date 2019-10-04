package com.ang.acb.youtubelearningbuddy.di;

import android.app.Application;

import androidx.room.Room;

import com.ang.acb.youtubelearningbuddy.BuildConfig;
import com.ang.acb.youtubelearningbuddy.data.local.db.AppDatabase;
import com.ang.acb.youtubelearningbuddy.data.local.dao.CommentDao;
import com.ang.acb.youtubelearningbuddy.data.local.dao.TopicDao;
import com.ang.acb.youtubelearningbuddy.data.local.dao.VideoDao;
import com.ang.acb.youtubelearningbuddy.data.local.dao.VideoTopicJoinDao;
import com.ang.acb.youtubelearningbuddy.data.remote.ApiService;
import com.ang.acb.youtubelearningbuddy.utils.LiveDataCallAdapterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * We annotate this class with @Module to signal to Dagger to search within the available
 * methods for possible instance providers. The methods that will actually expose available
 * return types should also be annotated with the @Provides annotation. The @Singleton
 * annotation also signals to the Dagger compiler that the instance should be created
 * only once in the application.
 *
 * See: https://github.com/codepath/android_guides/wiki/Dependency-Injection-with-Dagger-2
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "youtube.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    CommentDao provideCommentDao(AppDatabase database) {
        return database.commentDao();
    }

    @Provides
    @Singleton
    TopicDao provideTopicDao(AppDatabase database) {
        return database.topicDao();
    }

    @Provides
    @Singleton
    VideoDao provideVideoDao(AppDatabase database) {
        return database.videoDao();
    }

    @Provides
    @Singleton
    VideoTopicJoinDao provideVideoTopicJoinDao(AppDatabase database) {
        return database.videoTopicJoinDao();
    }

    @Provides
    @Singleton
    Interceptor provideApiRequestInterceptor() {
        // Because we are requesting an API which accepts an API key as a request parameter,
        // we can use an interceptor that could add the query parameter to every request method.
        // See: https://futurestud.io/tutorials/retrofit-2-how-to-add-query-parameters-to-every-request
        return new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.YOU_TUBE_API_KEY)
                        .build();

                Request request = original.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Interceptor apiRequestInterceptor) {
        // Retrofit 2 completely relies on OkHttp for any network operation.
        // Since logging isn’t integrated by default anymore in Retrofit 2,
        // we need to add a logging interceptor for OkHttp.
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // Set the desired log level. Warning: using the HEADERS or BODY levels
        // have the potential to leak sensitive information such as "Authorization"
        // or "Cookie" headers and the contents of request and response bodies.
        logging.level(HttpLoggingInterceptor.Level.BASIC);

        // Add the logging interceptor to our OkHttp client.
        return new OkHttpClient.Builder()
                .addInterceptor(apiRequestInterceptor)
                .addInterceptor(logging)
                .build();
    }

    @Provides
    @Singleton
    ApiService provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                // Configure which converter is used for the data serialization.
                .addConverterFactory(GsonConverterFactory.create())
                // Add a call adapter factory for supporting service method
                // return types other than Retrofit2.Call. We will use a custom
                // Retrofit adapter that converts the Retrofit2.Call into a
                // LiveData of ApiResponse.
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(client)
                .build()
                .create(ApiService.class);
    }
}
