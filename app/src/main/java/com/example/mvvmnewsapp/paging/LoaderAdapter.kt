package com.example.mvvmnewsapp.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.databinding.LoaderItemBinding

class LoaderAdapter: LoadStateAdapter<LoaderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LoaderItemBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(loadState: LoadState){
                binding.progressbar.isVisible = loadState is LoadState.Loading
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = LoaderItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }
}