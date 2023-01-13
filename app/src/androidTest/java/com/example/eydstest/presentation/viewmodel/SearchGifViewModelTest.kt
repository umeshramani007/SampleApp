package com.example.eydstest.presentation.viewmodel

import android.content.Context
import android.media.Image
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eydstest.FakeGiphyRepositoryImpl
import com.example.eydstest.data.model.*
import com.example.eydstest.data.repository.GiphyRepository
import com.example.eydstest.data.storage.GifDatabase
import com.example.eydstest.data.storage.dao.GifDao
import com.example.eydstest.getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class SearchGifViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: SearchGifViewModel
    lateinit var gifDao: GifDao
    lateinit var db: GifDatabase
    lateinit var fakeRepository: FakeGiphyRepositoryImpl

    @Before
    fun initialize() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, GifDatabase::class.java
        ).build()
        gifDao = db.gifDao()
        fakeRepository = FakeGiphyRepositoryImpl(gifDao)

        viewModel = SearchGifViewModel(fakeRepository, gifDao)
    }

    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun testToCheckCorrectOffset() = runBlocking {
        val response = fakeRepository.fetchGIFs("dummy", 50)
        assertTrue(((response as Resource.Success).data as ApiResponse).isSuccessful())
        assertTrue((response.data as ApiResponse).pagination?.offset == 100)
    }

    @Test
    fun testTrending() {
        viewModel.fetchTrendingGif()
        viewModel.fetchTrendingGif()
        assertTrue(viewModel.trendingGifList.getOrAwaitValue().size > 0)
    }
    @Test
    fun testToCheckForLastPage() {
        viewModel.searchGif("dummy")
        viewModel.searchGif("dummy")
        viewModel.searchGif("dummy")
        viewModel.searchGif("dummy")
        assertTrue(viewModel.error.getOrAwaitValue() == "")
    }

    @Test
    fun testToCheckForDifferentSearchQuery() {
        viewModel.searchGif("dummy")
        viewModel.searchGif("funny")
        assertTrue(viewModel.searchList.getOrAwaitValue().size == 50)
    }

    @Test
    fun testToAddToFavorite() {
        var gifObject = GIFObject("1", Images(PreviewWebP("")))
        viewModel.toggleFavorite(gifObject)
        assertTrue(gifDao.getFavoriteGifs().getOrAwaitValue().isNotEmpty())
    }

    @Test
    fun testToRemoveFromFavorite() {
        var gifObject = GIFObject("1", Images(PreviewWebP("")))
        viewModel.toggleFavorite(gifObject)

        gifObject.isFavorite = true
        viewModel.toggleFavorite(gifObject)
        assertTrue(gifDao.getFavoriteGifs().getOrAwaitValue().isEmpty())
    }
}