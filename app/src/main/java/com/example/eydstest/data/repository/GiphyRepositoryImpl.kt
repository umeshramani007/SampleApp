package com.example.eydstest.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.eydstest.data.model.GIFObject
import com.example.eydstest.data.model.Resource
import com.example.eydstest.data.remote.GiphyAPIService
import com.example.eydstest.data.storage.dao.GifDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GiphyRepositoryImpl(private val apiService: GiphyAPIService, private val gifDao: GifDao) :
    GiphyRepository {

    override suspend fun fetchGIFs(searchString: String, offset: Int): Resource {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiService.searchGif(searchString, offset))
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message)
            }
        }
    }

    override suspend fun fetchTrendingGIFs(offset: Int): Resource {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiService.fetchTrendingGif(offset))
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message)
            }
        }
    }

    override suspend fun addToFavorite(favoriteGif: GIFObject) {
        gifDao.insertGifToFavorite(favoriteGif)
    }

    override suspend fun removeFromFavorite(favoriteGif: GIFObject) {
        gifDao.removeFavorite(favoriteGif)
    }
}