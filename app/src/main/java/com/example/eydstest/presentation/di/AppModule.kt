package com.example.eydstest.ui.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eydstest.data.remote.GiphyAPIService
import com.example.eydstest.data.repository.GiphyRepository
import com.example.eydstest.data.repository.GiphyRepositoryImpl
import com.example.eydstest.data.storage.GifDatabase
import com.example.eydstest.data.storage.dao.GifDao
import com.example.eydstest.presentation.util.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideGSON(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }



    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GifDatabase {
        return Room.databaseBuilder(
            context,
            GifDatabase::class.java,
            "GifDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGifDao(database: GifDatabase): GifDao =
        database.gifDao()

    @Provides
    @Singleton
    fun provideGifRepository(apiService: GiphyAPIService, gifDao: GifDao): GiphyRepository {
        return GiphyRepositoryImpl(apiService, gifDao)
    }

    @Provides
    @Singleton
    fun provideGiphyApiService(retrofit: Retrofit): GiphyAPIService {
        return retrofit.create(GiphyAPIService::class.java)
    }
}

