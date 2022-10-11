package com.borabor.travelguideapp.presentation.ui.searchresult

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentSearchResultBinding
import com.borabor.travelguideapp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {

    override val viewModel: SearchResultViewModel by viewModels()

    private lateinit var adapter: SearchResultAdapter

    private var query = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        setupToolbar()
        setupAdapter()
        subscribeToObservable()

        binding.apiResponseState.btRetry.setOnClickListener {
            viewModel.retryConnection {
                viewModel.fetchTravelListWithQuery(query)
            }
        }
    }

    private fun getArgs() {
        val args = arguments?.let { SearchResultFragmentArgs.fromBundle(it) }
        args?.let {
            binding.title = it.title
            viewModel.fetchTravelListWithQuery(it.query)
            query = it.query
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbar.apply {
            navigationIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.blue4))
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupAdapter() {
        adapter = SearchResultAdapter { travel ->
            val action = SearchResultFragmentDirections.actionGlobalDetailFragment(travel)
            findNavController().navigate(action)
        }

        binding.rvSearchResult.adapter = adapter
    }

    private fun subscribeToObservable() {
        viewModel.searchResult.observe(viewLifecycleOwner) { searchResult ->
            searchResult?.let {
                if (it.isEmpty()) binding.tvNoResults.visibility = View.VISIBLE
                else adapter.submitList(searchResult)
            }
        }
    }
}