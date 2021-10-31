package ru.netology.papillon.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.papillon.BuildConfig
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.dto.User

private const val BASE_URL = "${BuildConfig.BASE_URL}/api"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface ApiService {
// integration with posts

    @GET("posts")
    suspend fun getAllPosts(): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getByIdPost(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun savePost(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removedByIdPost(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likedByIdPost(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikedByIdPost(@Path("id") id: Long): Response<Post>

    @POST("posts/{id}/shares")
    suspend fun sharedByIdPost(@Path("id") id: Long): Response<Post>

    @POST("posts/{id}/views")
    suspend fun viewedByIdPost(@Path("id") id: Long): Response<Post>

// integration with jobs

    @GET("jobs")
    suspend fun getAllJobs(): Response<List<Job>>

    @POST("jobs")
    suspend fun saveJob(@Body job: Job): Response<Job>

    @DELETE("jobs/{id}")
    suspend fun removedByIdJob(@Path("id") id: Long): Response<Unit>

    // integration with users

    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    @POST("users")
    suspend fun saveUser(@Body user: User): Response<User>

    @DELETE("users/{id}")
    suspend fun removedByIdUser(@Path("id") id: Long): Response<Unit>
}

object Api {
    val service by lazy {
        retrofit.create(ApiService::class.java)
    }
}