package com.example.eydstest.data.remote

import com.example.eydstest.data.model.ApiResponse
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyAPIService {

    @GET("search?api_key=NgNJTMNdiLJuwankk7fcbKV9PH7dUsq7&?limit=30")
    suspend fun searchGif(@Query("q") searchString: String, @Query("offset") offset: Int): ApiResponse

    @GET("trending?api_key=NgNJTMNdiLJuwankk7fcbKV9PH7dUsq7&?limit=30")
    suspend fun fetchTrendingGif(
        @Query("offset") offset: Int
    ): ApiResponse
}