package com.example.mvvmnewsapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.mvvmnewsapp.network.NewsApi
import com.example.mvvmnewsapp.constants.Constants.Companion.BREAKING_NEWS_PAGE_SIZE
import com.example.mvvmnewsapp.constants.Constants.Companion.SEARCH_NEWS_PAGE_SIZE
import com.example.mvvmnewsapp.model.Article
import com.example.mvvmnewsapp.paging.NewsPagingSource
import com.example.mvvmnewsapp.paging.SearchNewsPagingSource
import com.example.mvvmnewsapp.room.ArticleDB
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi, private val articleDB: ArticleDB) {

    fun getNews() = Pager(
        config = PagingConfig(BREAKING_NEWS_PAGE_SIZE, maxSize = 100),
        pagingSourceFactory = { NewsPagingSource(newsApi) }
    ).liveData

    fun getNewsSearch(text: String) = Pager(
        config = PagingConfig(SEARCH_NEWS_PAGE_SIZE, maxSize = 300),
        pagingSourceFactory = { SearchNewsPagingSource(newsApi, text) }
    ).liveData

    fun getSavedNews() = articleDB.provideArticleDatabase().getAllArticles()

    suspend fun upsert(article: Article) = articleDB.provideArticleDatabase().upsert(article)

    suspend fun deleteArticle(article: Article) = articleDB.provideArticleDatabase().deleteArticle(article)
}