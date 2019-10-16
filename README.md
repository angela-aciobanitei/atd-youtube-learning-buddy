## Android Techdegree Treehouse Project 10: YouTube Learning Buddy

YouTube is a great resource for learning. Unfortunately, YouTube is also a great resource for distractions. For this project, we’d like you to build an app to help give users a distraction free learning experience with YouTube. You’ll do this by letting users curate their own learning. Users should be able to create ‘Topics’, as well as search for videos to add to those topics. Users will then be able to select a topic and start watching videos right from within your app! Also, make sure the user has a way to see the video description, and don’t forget an option to view the comments! 


## Project Instructions

*   Create the application with the ability to search for videos using the **[YouTube API](https://developers.google.com/youtube/v3)**.
*   When the results are returned, allow users to add videos to user-specified topic areas.
*   After a topic area is selected, the user should be able to watch those videos from inside the app using the **[YouTubePlayerView](https://developers.google.com/youtube/android/player/reference/com/google/android/youtube/player/YouTubePlayerView)**.
*   The application needs to utilize **[Retrofit](https://square.github.io/retrofit/)** to consume the YouTube API.
*   The application needs to utilize **[RxJava](https://github.com/ReactiveX/RxJava)** or **[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)** to setup and use an **[observer pattern](https://www.oodesign.com/observer-pattern.html)**.


## Architectural Patterns
*   A single-activity architecture, using the **[Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)** to manage fragment operations.
*   A **presentation layer** that contains a fragment (View) and a ViewModel per screen (or feature).
*   Reactive UIs using **[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)** observables and **[DataBinding](https://developer.android.com/topic/libraries/data-binding/)**.
*   A **data layer** with a repository and two data sources (local using **[Room](https://developer.android.com/topic/libraries/architecture/room)** and remote using **[Retrofit](https://github.com/square/retrofit)** ).


## Libraries
*   **[AndroidX](https://developer.android.com/jetpack/androidx/)**
*   **[Data Binding](https://developer.android.com/topic/libraries/data-binding/)**
*   **[Navigation Component](https://developer.android.com/guide/navigation/)**
*   **[Room](https://developer.android.com/topic/libraries/architecture/room)**, a SQLite object mapping library.
*   **[Retrofit 2](https://github.com/square/retrofit)**, a type-safe HTTP client for Android and Java.
*   **[OkHttp](https://github.com/square/okhttp)**, an HTTP client for Android, Kotlin, and Java. 
*   **[Gson](https://github.com/google/gson)**, a Java serialization/deserialization library to convert Java Objects into JSON and back.
*   **[Glide](https://github.com/bumptech/glide)**, an image loading and caching library for Android.
*   **[Timber](https://github.com/JakeWharton/timber)**, a logger that provides utility on top of Android's normal Log class. 
*   **[Dagger 2](https://github.com/google/dagger)**, a fast dependency injector for Android and Java.  
