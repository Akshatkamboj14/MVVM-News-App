package com.example.mvvmnewsapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvmnewsapp.model.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDB : RoomDatabase() {
    abstract fun provideArticleDatabase(): ArticleDao
}