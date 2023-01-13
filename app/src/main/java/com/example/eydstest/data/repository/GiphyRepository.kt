package com.example.eydstest.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.eydstest.data.model.ApiResponse
import com.example.eydstest.data.model.GIFObject
import com.example.eydstest.data.model.Resource

interface GiphyRepository {
//    suspend fun fetchTrendingGIFs(offset: Int): ApiResponse
    //    suspend fun fetchGIFs(searchString: String, offset: Int): ApiResponse
    suspend fun fetchGIFs(searchString: String, offset: Int): Resource
    suspend fun fetchTrendingGIFs(offset: Int): Resource
    suspend fun addToFavorite(favoriteGif: GIFObject)
    suspend fun removeFromFavorite(favoriteGif: GIFObject)
}