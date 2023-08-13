package com.example.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.ui.MainActivity
import com.example.mvvmnewsapp.R
import kotlinx.coroutines.delay
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mvvmnewsapp.constants.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mvvmnewsapp.databinding.FragmentSearchBinding
import com.example.mvvmnewsapp.paging.LoaderAdapter
import com.example.mvvmnewsapp.paging.NewsPagingAdapter
import com.example.mvvmnewsapp.viewmodels.NewsViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsPagingAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvSearchNews
        newsAdapter = NewsPagingAdapter(requireContext(), view)
        viewModel = ViewModelProvider(activity as MainActivity)[NewsViewModel::class.java]

        recyclerView.layoutManager = CardStackLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = newsAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment, bundle)
        }
        newsAdapter.setOnSaveClickListener(viewModel)

        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                it?.let { it ->
                    if (it.toString().isNotEmpty()){
                        viewModel.searchList(it.toString()).observe(activity as MainActivity, Observer {
                            newsAdapter.submitData((activity as MainActivity).lifecycle, it)
                        })
                    }
                }
            }
        }
    }

}