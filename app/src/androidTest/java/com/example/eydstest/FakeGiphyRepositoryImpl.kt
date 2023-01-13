package com.example.eydstest

import androidx.lifecycle.LiveData
import com.example.eydstest.data.model.*
import com.example.eydstest.data.repository.GiphyRepository
import com.example.eydstest.data.storage.dao.GifDao
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class FakeGiphyRepositoryImpl(val gifDao: GifDao) : GiphyRepository {
    override suspend fun fetchGIFs(searchString: String, offset: Int): Resource {
        val gifList = ArrayList<GIFObject>()
        for (i in 0..49)
            gifList.add(
                GIFObject(
                    i.toString(),
                    Images(PreviewWebP(url = "https://media.tenor.com/eN29B8kH4mAAAAAi/cute-bear.gif"))
                )
            )
        val metaData = MetaData(200, "Success")
        val pagination = Pagination(offset + 50, 210, 50)
        return Resource.Success(ApiResponse(gifList, metaData, pagination))
    }

    override suspend fun fetchTrendingGIFs(offset: Int): Resource {
        val gifList = ArrayList<GIFObject>()
        for (i in 0..49)
            gifList.add(
                GIFObject(
                    i.toString(),
                    Images(PreviewWebP(url = "https://media.tenor.com/eN29B8kH4mAAAAAi/cute-bear.gif"))
                )
            )
        val metaData = MetaData(200, "Success")
        val pagination = Pagination(50, 210, 50)
        return Resource.Success(ApiResponse(gifList, metaData, pagination))
    }

    override suspend fun addToFavorite(favoriteGif: GIFObject) {
        gifDao.insertGifToFavorite(favoriteGif)
    }

    override suspend fun removeFromFavorite(favoriteGif: GIFObject) {
        gifDao.removeFavorite(favoriteGif)
    }

    companion object {

        const val sample_response = "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": \"1\",\n" +
                "      \"preview_webp\": {\n" +
                "        \"url\": \"https://media0.giphy.com/media/BK0BeohHpYoBq/giphy-preview.webp?cid=ae69a1f71au7zw8f4dpyy3ourldjk1hwtotx2639wx4212s9&rid=giphy-preview.webp&ct=g\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"2\",\n" +
                "      \"preview_webp\": {\n" +
                "        \"url\": \"https://media0.giphy.com/media/BK0BeohHpYoBq/giphy-preview.webp?cid=ae69a1f71au7zw8f4dpyy3ourldjk1hwtotx2639wx4212s9&rid=giphy-preview.webp&ct=g\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"3\",\n" +
                "      \"preview_webp\": {\n" +
                "        \"url\": \"https://media0.giphy.com/media/BK0BeohHpYoBq/giphy-preview.webp?cid=ae69a1f71au7zw8f4dpyy3ourldjk1hwtotx2639wx4212s9&rid=giphy-preview.webp&ct=g\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"4\",\n" +
                "      \"preview_webp\": {\n" +
                "        \"url\": \"https://media0.giphy.com/media/BK0BeohHpYoBq/giphy-preview.webp?cid=ae69a1f71au7zw8f4dpyy3ourldjk1hwtotx2639wx4212s9&rid=giphy-preview.webp&ct=g\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"5\",\n" +
                "      \"preview_webp\": {\n" +
                "        \"url\": \"https://media0.giphy.com/media/BK0BeohHpYoBq/giphy-preview.webp?cid=ae69a1f71au7zw8f4dpyy3ourldjk1hwtotx2639wx4212s9&rid=giphy-preview.webp&ct=g\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"pagination\": {\n" +
                "    \"total_count\": 210,\n" +
                "    \"count\": 25,\n" +
                "    \"offset\": 0\n" +
                "  },\n" +
                "  \"meta\": {\n" +
                "    \"status\": 200,\n" +
                "    \"msg\": \"OK\",\n" +
                "    \"response_id\": \"1au7zw8f4dpyy3ourldjk1hwtotx2639wx4212s9\"\n" +
                "  }\n" +
                "}"
    }
}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : androidx.lifecycle.Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    try {
        afterObserve.invoke()
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}