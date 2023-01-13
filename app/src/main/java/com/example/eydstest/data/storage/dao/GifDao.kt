package com.example.eydstest.data.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.eydstest.data.model.GIFObject

@Dao
interface GifDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGifToFavorite(gif: GIFObject)

    @Delete
    fun removeFavorite(gif: GIFObject)

    @Query("SELECT * FROM gif_object")
    fun getFavoriteGifs(): LiveData<List<GIFObject>>
}