package com.borabor.travelguideapp.presentation.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentSearchBinding
import com.borabor.travelguideapp.presentation.ui.home.HomeFragmentDirections
import com.borabor.travelguideapp.util.checkQueryTextAndProceed
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapterTopDest: TopDestAdapter
    private lateinit var adapterNearby: NearbyAdapter

    private var snackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater).apply {
            lifecycleOwner = this@SearchFragment
            viewModel = this@SearchFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchBar()
        setupAdapters()
        subscribeToObservables()
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

        adapterNearby = NearbyAdapter({ viewModel.updateBookmark(it) }) { travel ->
            val action = HomeFragmentDirections.actionGlobalDetailFragment(travel)
            findNavController().navigate(action)
        }

        binding.rvNearby.adapter = adapterNearby
        binding.rvNearby.itemAnimator = null
    }

    private fun navigateWithQuery() {
        binding.search.etSearch.checkQueryTextAndProceed(this) { query ->
            val action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(query, getString(R.string.search_results_for, query))
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
            if (bookmarkState.isError) Toast.makeText(requireContext(), bookmarkState.errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState.isError) {
                snackbar = Snackbar.make(requireView(), uiState.errorMessage!!, Snackbar.LENGTH_INDEFINITE)
                    .setAnchorView(R.id.bottomNav)
                    .setAction(R.string.retry) { viewModel.retry() }

                snackbar!!.show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        snackbar?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}