package com.borabor.travelguideapp.presentation.ui.guide

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentGuideBinding
import com.borabor.travelguideapp.domain.model.Category
import com.borabor.travelguideapp.presentation.base.BaseFragment
import com.borabor.travelguideapp.util.checkQueryTextAndProceed
import com.borabor.travelguideapp.util.handleBookmarkState
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideFragment : BaseFragment<FragmentGuideBinding>(R.layout.fragment_guide) {

    override val viewModel: GuideViewModel by viewModels()

    private lateinit var adapterMightNeed: MightNeedAdapter
    private lateinit var adapterTopPick: TopPickAdapter

    private lateinit var ivBookmark: ImageView
    private lateinit var pbLoading: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupSearchBar()
        subscribeToObservables()

        binding.apiResponseState.btRetry.setOnClickListener {
            viewModel.retryConnection {
                viewModel.fetchLists()
            }
        }
    }

    private fun setupAdapters() {
        adapterMightNeed = MightNeedAdapter { travel ->
            val action = GuideFragmentDirections.actionGlobalDetailFragment(travel)
            findNavController().navigate(action)
        }

        binding.rvMightNeed.adapter = adapterMightNeed

        adapterTopPick = TopPickAdapter(
            { ivBookmark, pbLoading, id ->
                this.ivBookmark = ivBookmark
                this.pbLoading = pbLoading

                viewModel.updateBookmark(id)
            }
        ) { travel ->
            val action = GuideFragmentDirections.actionGlobalDetailFragment(travel)
            findNavController().navigate(action)
        }

        binding.rvTopPick.adapter = adapterTopPick
        binding.rvTopPick.itemAnimator = null
    }

    private fun setupSearchBar() {
        binding.search.apply {
            etSearch.setOnEditorActionListener { _, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_NEXT || ((keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) && (keyEvent.action == KeyEvent.ACTION_DOWN))) {
                    navigateWithQuery()
                    true
                } else false
            }

            ivSearch.setOnClickListener {
                navigateWithQuery()
            }
        }
    }

    private fun setupChips(categoryList: List<Category>) {
        categoryList.forEach { category ->
            binding.cgCategories.addView(
                Chip(context).apply {
                    setChipBackgroundColorResource(R.color.white2)
                    setChipStrokeColorResource(R.color.gray2)
                    chipStrokeWidth = 1f
                    setChipIconResource(setChipIconByCategoryId(category.id))
                    setChipIconTintResource(R.color.blue)
                    setIconStartPaddingResource(R.dimen.chip_icon_start_padding)
                    setTextAppearance(R.style.ChipText)
                    text = category.title
                    setOnClickListener {
                        Toast.makeText(context, category.title, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun setChipIconByCategoryId(id: String): Int {
        return when (id.toInt()) {
            1 -> R.drawable.ic_taxi
            2 -> R.drawable.ic_car
            3 -> R.drawable.ic_museum
            4 -> R.drawable.ic_restaurant
            5 -> R.drawable.ic_resort
            6 -> R.drawable.ic_mall
            else -> R.drawable.ic_sightseeing
        }
    }

    private fun navigateWithQuery() {
        binding.search.etSearch.checkQueryTextAndProceed(this) { query ->
            val action = GuideFragmentDirections.actionGuideFragmentToSearchResultFragment(query, getString(R.string.search_results_for, query))
            findNavController().navigate(action)
        }
    }

    private fun subscribeToObservables() {
        viewModel.mightNeedList.observe(viewLifecycleOwner) { mightNeedList ->
            adapterMightNeed.submitList(mightNeedList)
        }

        viewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
            setupChips(categoryList)
        }

        viewModel.topPickList.observe(viewLifecycleOwner) { topPickList ->
            adapterTopPick.submitList(topPickList)
        }

        viewModel.bookmarkState.observe(viewLifecycleOwner) { bookmarkState ->
            if (this::ivBookmark.isInitialized && this::pbLoading.isInitialized) {
                bookmarkState?.handleBookmarkState(this, ivBookmark, pbLoading)
            }
        }
    }
}