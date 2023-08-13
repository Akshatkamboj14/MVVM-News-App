package com.example.mvvmnewsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mvvmnewsapp.constants.Constants.Companion.BREAKING_NEWS_PAGE_SIZE
import com.example.mvvmnewsapp.model.Article
import com.example.mvvmnewsapp.network.NewsApi

class NewsPagingSource(val newsApi: NewsApi): PagingSource<Int,Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try{
            val position = params.key ?: 1
            val response = newsApi.getBreakingNews(pageNumber = position)
            LoadResult.Page(
                data = response.articles,
                prevKey = if (position == 1) null else position.minus(1),
                nextKey = if (position == (response.totalResults/BREAKING_NEWS_PAGE_SIZE).plus(1)) null else position.plus(1)
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}
