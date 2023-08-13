package com.example.mvvmnewsapp.model

data class NewsData(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)