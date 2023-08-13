package com.example.mvvmnewsapp.paging

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.databinding.NewsItemBinding
import com.example.mvvmnewsapp.model.Article
import com.example.mvvmnewsapp.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.qualifiers.ActivityContext

class NewsPagingAdapter(@ActivityContext val context: Context, private val view: View) :
    PagingDataAdapter<Article, NewsPagingAdapter.ViewHolder>(differCallbacks) {

    inner class ViewHolder(val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((Article) -> Unit)? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            holder.binding.apply {
                mainCard.setCardBackgroundColor(context.getColor(backgroundColor(position)))
                with(holder) {
                    Glide.with(this.itemView).load(item.urlToImage)
                        .placeholder(R.drawable.baseline_broken_image_24).into(binding.imgArticle)
                    with(this.itemView){
                        setOnClickListener {
                            onItemClickListener?.let { it(item) }
                        }
                    }
                }
                if (item.content.isNullOrEmpty()) titleTv.text = context.getString(R.string.title) else titleTv.text = item.content

                if (item.publishedAt.isNullOrEmpty()) publishedAtTv.text = context.getString(R.string.published_at) else publishedAtTv.text = item.publishedAt

                if (item.author.isNullOrEmpty())  publishedAtDataTv.text = context.getString(R.string.published_by) else publishedAtDataTv.text = item.author

                if (item.description.isNullOrEmpty()) descriptionTv.text = context.getString(R.string.description) else  descriptionTv.text = item.description

                iconDelete.visibility = View.GONE
                iconSaved.visibility = View.VISIBLE
                iconSaved.setOnClickListener {
                    it.background = AppCompatResources.getDrawable(context, R.drawable.baseline_bookmark_24)
                    onSaveClickListener?.saveArticle(item)
                    Snackbar.make(view,"Article Saved Successfully", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun backgroundColor(position: Int): Int {
        val list = listOf<Int>(R.color.first, R.color.second, R.color.third)
        val colorIndex: Int = position % list.size
        return list[colorIndex]
    }

    private var onSaveClickListener: NewsViewModel? = null

    fun setOnSaveClickListener(viewModel: NewsViewModel) {
        onSaveClickListener = viewModel
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    companion object {
        private var differCallbacks = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

}