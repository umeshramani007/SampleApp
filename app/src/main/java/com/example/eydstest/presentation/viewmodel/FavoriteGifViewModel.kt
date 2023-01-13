package com.example.eydstest.presentation.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.paging.cachedIn
import com.example.eydstest.data.model.GIFObject
import com.example.eydstest.data.model.Pagination
import com.example.eydstest.data.repository.GiphyRepository
import com.example.eydstest.data.storage.dao.GifDao
import com.example.eydstest.presentation.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FavoriteGifViewModel @Inject constructor(
    private val gifDao: GifDao
) :
    ViewModel() {

    var favoriteGifList: LiveData<List<GIFObject>> = gifDao.getFavoriteGifs()

    fun removeFromFavorite(gifObject: GIFObject) {
        viewModelScope.launch(Dispatchers.IO) {
                gifDao.removeFavorite(gifObject)
        }
    }
}