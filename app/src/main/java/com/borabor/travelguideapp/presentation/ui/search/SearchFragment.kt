package com.borabor.travelguideapp.presentation.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentSearchBinding
import com.borabor.travelguideapp.presentation.base.BaseFragment
import com.borabor.travelguideapp.presentation.ui.home.HomeFragmentDirections
import com.borabor.travelguideapp.util.ListType
import com.borabor.travelguideapp.util.checkQueryTextAndProceed
import com.borabor.travelguideapp.util.handleBookmarkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    override val viewModel: SearchViewModel by viewModels()

    private lateinit var adapterTopDest: TopDestAdapter
    private lateinit var adapterNearby: NearbyAdapter

    private lateinit var ivBookmark: ImageView
    private lateinit var pbLoading: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupSearchBar()
        setupAdapters()
        subscribeToObservables()
    }

    private fun setupClickListeners() {
        binding.apiResponseState.btRetry.setOnClickListener {
            viewModel.retryConnection {
                viewModel.fetchLists()
            }
        }
    }

    private fun setupSearchBar() {
        binding.search.etSearch.setOnEditorActionListener { _, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_NEXT || ((keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) && (keyEvent.action == KeyEvent.ACTION_DOWN))) {
                navigateWithQuery()
                true
            } else false
        }

        binding.search.ivSearch.setOnClickListener {
            navigateWithQuery()
        }
    }

    private fun setupAdapters() {
        adapterTopDest = TopDestAdapter { travel ->
            val action = HomeFragmentDirections.actionGlobalDetailFragment(travel)
            findNavController().navigate(action)
        }

        binding.rvTopDest.adapter = adapterTopDest

        adapterNearby = NearbyAdapter({ ivBookmark, pbLoading, id, isBookmark ->
            this.ivBookmark = ivBookmark
            this.pbLoading = pbLoading

            viewModel.bookmark(id, isBookmark)
        })
        { travel ->
            val action = HomeFragmentDirections.actionGlobalDetailFragment(travel)
            findNavController().navigate(action)
        }

        binding.rvNearby.adapter = adapterNearby
        binding.rvNearby.itemAnimator = null
    }

    private fun navigateWithQuery() {
        binding.search.etSearch.checkQueryTextAndProceed(this) { query ->
            val action = SearchFragmentDirections.actionGlobalSearchResultFragment(listType = ListType.ALL, query = query)
            findNavController().navigate(action)
        }
    }

    private fun subscribeToObservables() {
        viewModel.topDestList.observe(viewLifecycleOwner) { topDestinations ->
            adapterTopDest.submitList(topDestinations)
        }

        viewModel.nearbyList.observe(viewLifecycleOwner) { nearbyAttractions ->
            adapterNearby.submitList(nearbyAttractions)
        }

        viewModel.bookmarkState.observe(viewLifecycleOwner) { bookmarkState ->
            if (this::ivBookmark.isInitialized && this::pbLoading.isInitialized) {
                bookmarkState?.handleBookmarkState(this, ivBookmark, pbLoading)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchLists()
    }
}