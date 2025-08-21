package com.homeworks.finalexam

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/popular")
    suspend fun getPopular(): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlaying(): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(): MoviesResponse

    @GET("movie/{id}")
    suspend fun getDetails(
        @Path("id") id: Int
    ): MovieDetail

    @GET("movie/{id}/credits")
    suspend fun getCredits(
        @Path("id") id: Int
    ): CreditsResponse

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create(): TmdbApi {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val authInterceptor = Interceptor { chain ->
                val original = chain.request()
                val req = original.newBuilder()
                    .header("Authorization", "Bearer ${BuildConfig.TMDB_BEARER_TOKEN}")
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(req)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TmdbApi::class.java)
        }
    }
}


