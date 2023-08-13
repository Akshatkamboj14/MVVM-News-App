package com.example.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapter.SaveNewsAdapter
import com.example.mvvmnewsapp.databinding.FragmentSaveNewsBinding
import com.example.mvvmnewsapp.ui.MainActivity
import com.example.mvvmnewsapp.viewmodels.NewsViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveNewsFragment : Fragment() {

    private lateinit var binding: FragmentSaveNewsBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var saveNewsAdapter: SaveNewsAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveNewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvSavedNews
        saveNewsAdapter = SaveNewsAdapter(requireContext(), view)

        viewModel = ViewModelProvider(activity as MainActivity)[NewsViewModel::class.java]

        recyclerView.layoutManager = CardStackLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = saveNewsAdapter

        saveNewsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_saveNewsFragment_to_articleFragment, bundle)
        }

        saveNewsAdapter.setOnDeleteClickListener(viewModel)

        viewModel.getSavedNews().observe(activity as MainActivity, Observer {
            saveNewsAdapter.differ.submitList(it)
        })
    }
}