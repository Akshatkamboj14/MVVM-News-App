package com.example.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.ui.MainActivity
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.example.mvvmnewsapp.paging.LoaderAdapter
import com.example.mvvmnewsapp.paging.NewsPagingAdapter
import com.example.mvvmnewsapp.viewmodels.NewsViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NewsPagingAdapter
    lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBreakingNewsBinding.inflate(inflater)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.rvBreakingNews
        adapter = NewsPagingAdapter(requireContext(), view)
        newsViewModel = ViewModelProvider(activity as MainActivity)[NewsViewModel::class.java]

        recyclerView.layoutManager = CardStackLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle)
        }

        adapter.setOnSaveClickListener(newsViewModel)

        newsViewModel.list.observe(activity as MainActivity, Observer {
            adapter.submitData((activity as MainActivity).lifecycle, it)
        })
    }

}