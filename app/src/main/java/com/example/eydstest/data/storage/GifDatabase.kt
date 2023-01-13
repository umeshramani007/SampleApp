package com.example.eydstest.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eydstest.data.model.GIFObject
import com.example.eydstest.data.storage.dao.GifDao

@Database(entities = [GIFObject::class], version = 1)
abstract class GifDatabase : RoomDatabase() {
    abstract fun gifDao(): GifDao
}