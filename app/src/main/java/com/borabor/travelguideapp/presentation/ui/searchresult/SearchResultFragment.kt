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
import com.borabor.travelguideapp.presentation.ui.home.HomeFragment
import com.borabor.travelguideapp.presentation.ui.home.HomeFragment.DealType.*
import com.borabor.travelguideapp.util.ListType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {

    override val viewModel: SearchResultViewModel by viewModels()

    private lateinit var adapter: SearchResultAdapter

    private var listType = ListType.ALL
    private var dealType = FLIGHTS
    private var query = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        setClickListeners()
        setupToolbar()
        setupAdapter()
        subscribeToObservable()
    }

    private fun getArgs() {
        val args = arguments?.let { SearchResultFragmentArgs.fromBundle(it) }
        args?.let {
            listType = it.listType
            dealType = it.dealType
            query = it.query

            binding.title = setTitle(listType, dealType)
            viewModel.getResults(listType, dealType, query)
        }
    }

    private fun setTitle(searchType: ListType, dealType: HomeFragment.DealType) = when (searchType) {
        ListType.ALL -> getString(R.string.search_results_for, query)
        ListType.MIGHT_NEEDS -> getString(R.string.might_need_these).lowercase().capitalize()
        else -> when (dealType) {
            FLIGHTS -> getString(R.string.flights)
            HOTELS -> getString(R.string.hotels)
            TRANSPORTATIONS -> getString(R.string.transportations)
        }
    }

    private fun setClickListeners() {
        binding.apiResponseState.btRetry.setOnClickListener {
            viewModel.retryConnection {
                viewModel.getResults(listType, dealType, query)
            }
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
        viewModel.resultList.observe(viewLifecycleOwner) { searchResult ->
            searchResult?.let {
                if (it.isEmpty()) binding.tvNoResults.visibility = View.VISIBLE
                else adapter.submitList(searchResult)
            }
        }
    }
}