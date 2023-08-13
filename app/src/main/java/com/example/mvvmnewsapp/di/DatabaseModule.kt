package com.example.mvvmnewsapp.di

import android.content.Context
import androidx.room.Room
import com.example.mvvmnewsapp.room.ArticleDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDB{
        return Room.databaseBuilder(
            context,
            ArticleDB::class.java,
            "article_db.db"
        ).build()
    }


}