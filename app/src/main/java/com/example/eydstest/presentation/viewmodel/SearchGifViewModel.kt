package com.example.eydstest.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.paging.cachedIn
import com.bumptech.glide.load.resource.gif.GifOptions
import com.example.eydstest.data.model.*
import com.example.eydstest.data.repository.GiphyRepository
import com.example.eydstest.data.storage.dao.GifDao
import com.example.eydstest.presentation.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchGifViewModel @Inject constructor(
    private val repository: GiphyRepository,
    private val gifDao: GifDao
) :
    ViewModel() {

    private var _searchList = MutableLiveData<List<GIFObject>>()
    var searchList: LiveData<List<GIFObject>> = _searchList

    private var _trendingGifList = MutableLiveData<List<GIFObject>>()
    var trendingGifList: LiveData<List<GIFObject>> = _trendingGifList

    private var prevSearchString: String = ""
    private var searchPagination: Pagination? = null
    private var trendingGifPagination: Pagination? = null

    private var _error = MutableLiveData<String>()
    var error: LiveData<String> = _error

    private var _favoriteGifList: LiveData<List<GIFObject>> = gifDao.getFavoriteGifs()

    private val favoriteGifObserver = Observer<List<GIFObject>>() {
        viewModelScope.launch {

            //Favorite Gif list is updated. Refresh both Search and Trending Gif list to show the Favorite Icon
            var list = (_trendingGifList.value as ArrayList<GIFObject>?) ?: ArrayList()
            refreshListForToggleFavorite(list, it)

            var favoriteList = (_searchList.value as ArrayList<GIFObject>?) ?: ArrayList()
            refreshListForToggleFavorite(favoriteList, it)

            //Update the already fetched list.
            //Here the sequence is important so that one list should not replce the other one
            if (prevSearchString.isNotEmpty()) {
                _trendingGifList.value = list
                _searchList.value = favoriteList
            } else {
                _searchList.value = favoriteList
                _trendingGifList.value = list
            }
        }
    }

    init {
        _favoriteGifList.observeForever(favoriteGifObserver)
    }

    fun searchGif(searchString: String) {
        viewModelScope.launch {

            //User has searched with a new query. Reset the pagination data
            if (prevSearchString != searchString) {
                searchPagination = null
                _searchList.value = ArrayList<GIFObject>()
            }

            var offset = 0
            if (searchPagination != null) {
                offset = searchPagination!!.offset + searchPagination!!.count
                if (offset > searchPagination!!.total_count) {
                    _error.value = ""
                    return@launch
                }
            }

            when (val response = repository.fetchGIFs(searchString, offset)) {
                is Resource.Error -> _error.value = response.errMessage
                is Resource.Success -> {
                    if (!(response.data as ApiResponse).isSuccessful()) {
                        _error.value = response.data.meta?.msg ?: Constant.ERROR
                        return@launch
                    }

                    val list =
                        appendData(_searchList.value as ArrayList<GIFObject>, response.data.data)
                    refreshListForToggleFavorite(list, _favoriteGifList.value as List<GIFObject>)
                    _searchList.value = list

                    searchPagination = response.data.pagination
                    prevSearchString = searchString
                }
            }

        }
    }

    fun fetchTrendingGif() {
        viewModelScope.launch {
            prevSearchString = ""
            searchPagination = null

            var offset = 0
            if (trendingGifPagination != null) {
                offset = trendingGifPagination!!.offset + trendingGifPagination!!.count
                if (offset > trendingGifPagination!!.total_count) {
                    _error.value = ""
                    return@launch
                }
            }

            when (val response = repository.fetchTrendingGIFs(offset)) {
                is Resource.Error -> _error.value = response.errMessage
                is Resource.Success -> {
                    if (!(response.data as ApiResponse).isSuccessful()) {
                        _error.value = response.data.meta?.msg ?: Constant.ERROR
                        return@launch
                    }

                    val list = appendData(
                        _trendingGifList.value as ArrayList<GIFObject>,
                        response.data.data
                    )
                    refreshListForToggleFavorite(list, _favoriteGifList.value as List<GIFObject>)

                    _trendingGifList.value = list

                    trendingGifPagination = response.data.pagination
                }
            }
        }
    }

    private fun refreshListForToggleFavorite(
        gifList: List<GIFObject>,
        favGifList: List<GIFObject>
    ) {
        if (gifList.isNotEmpty()) {
            val length = gifList.size
            for (i in 0 until length) {
                gifList[i].isFavorite = favGifList.contains(gifList[i])
            }
        }
    }

    private fun appendData(
        oldList: ArrayList<GIFObject>?,
        newList: List<GIFObject>?
    ): List<GIFObject> {
        var list = (oldList) ?: ArrayList()

        if (list.isNotEmpty() && list[oldList!!.size - 1].id == "-1") {
            list.removeAt(list.size - 1)
        }
        newList?.let {
            list.addAll(it)
        }
        return list
    }

    fun toggleFavorite(gifObject: GIFObject) {
        viewModelScope.launch(Dispatchers.IO) {
            if (gifObject.isFavorite) {
                gifDao.removeFavorite(gifObject)
            } else {
                gifDao.insertGifToFavorite(gifObject)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _favoriteGifList.removeObserver(favoriteGifObserver)
    }
}
