package com.github.kolesovv.news.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.github.kolesovv.news.data.local.NewsDao
import com.github.kolesovv.news.data.local.NewsDatabase
import com.github.kolesovv.news.data.remote.NewsApiService
import com.github.kolesovv.news.data.repository.NewsRepositoryImpl
import com.github.kolesovv.news.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

    companion object {

        @Singleton
        @Provides
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideJson(): Json {

            return Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }

        @Singleton
        @Provides
        fun provideConverter(json: Json): Converter.Factory {

            return json.asConverterFactory("application/json".toMediaType())
        }

        @Singleton
        @Provides
        fun provideRetrofit(converter: Converter.Factory): Retrofit {

            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(converter)
                .build()
        }

        @Singleton
        @Provides
        fun provideApiService(retrofit: Retrofit): NewsApiService {

            return retrofit.create()
        }

        @Singleton
        @Provides
        fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = NewsDatabase::class.java,
                name = "news.db"
            ).fallbackToDestructiveMigration(true).build()
        }

        @Singleton
        @Provides
        fun providesNewsDao(database: NewsDatabase): NewsDao {
            return database.NewsDao()
        }
    }
}
