package com.borabor.travelguideapp.presentation.ui.searchresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentSearchResultBinding
import com.borabor.travelguideapp.util.hideBottomNav
import com.borabor.travelguideapp.util.showBottomNav
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchResultViewModel by viewModels()

    private lateinit var adapter: SearchResultAdapter

    private var snackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchResultBinding.inflate(inflater).apply {
            lifecycleOwner = this@SearchResultFragment
            viewModel = this@SearchResultFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        setupToolbar()
        setupAdapter()
        subscribeToObservable()

        binding.apiResponseState.btRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    private fun getArgs() {
        val args = arguments?.let { SearchResultFragmentArgs.fromBundle(it) }
        args?.let {
            binding.title = it.title
            viewModel.fetchTravelListWithQuery(it.query)
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

    override fun onResume() {
        super.onResume()
        hideBottomNav()
    }

    override fun onPause() {
        super.onPause()
        snackbar?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        showBottomNav()
    }
}