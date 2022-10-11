package com.borabor.travelguideapp.presentation.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentHomeBinding
import com.borabor.travelguideapp.presentation.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: DealAdapter

    private var tabPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBannerButtons()
        setupTabLayout()
        setupAdapter()
        subscribeToObservables()

        binding.apiResponseState.btRetry.setOnClickListener {
            viewModel.retryConnection {
                viewModel.fetchDealList()
            }
        }
    }

    private fun setupBannerButtons() {
        binding.apply {
            flights.root.setOnClickListener {
                Toast.makeText(requireContext(), getString(R.string.flights), Toast.LENGTH_SHORT).show()
            }
            hotels.root.setOnClickListener {
                Toast.makeText(requireContext(), getString(R.string.hotels), Toast.LENGTH_SHORT).show()
            }
            cars.root.setOnClickListener {
                Toast.makeText(requireContext(), getString(R.string.cars), Toast.LENGTH_SHORT).show()
            }
            taxi.root.setOnClickListener {
                Toast.makeText(requireContext(), getString(R.string.taxi), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupTabLayout() {
        binding.tlDeals.getTabAt(tabPosition)?.select()
        binding.tlDeals.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.filterDeals(
                    when (tab?.position) {
                        1 -> "flight"
                        2 -> "hotel"
                        3 -> "transportation"
                        else -> "all"
                    }
                )

                binding.rvDeals.scrollToPosition(0)
                tabPosition = tab?.position ?: 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupAdapter() {
        adapter = DealAdapter { travel ->
            val action = HomeFragmentDirections.actionGlobalDetailFragment(travel)
            findNavController().navigate(action)
        }

        binding.rvDeals.adapter = adapter
    }

    private fun subscribeToObservables() {
        viewModel.dealList.observe(viewLifecycleOwner) { travelList ->
            adapter.submitList(travelList)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            tabPosition = it.getInt(TAB_POSITION)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_POSITION, tabPosition)
    }

    companion object {
        private const val TAB_POSITION = "tab_position"
    }
}