package com.borabor.travelguideapp.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentHomeBinding
import com.borabor.travelguideapp.presentation.base.BaseFragment
import com.borabor.travelguideapp.util.ListType
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: DealAdapter

    private var tabPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupBannerButtons()
        setupTabLayout()
        setupAdapter()
        subscribeToObservable()
    }

    private fun setupClickListeners() {
        binding.apiResponseState.btRetry.setOnClickListener {
            viewModel.retryConnection {
                viewModel.fetchDealList()
            }
        }
    }

    private fun setupBannerButtons() {
        binding.apply {
            flights.root.setOnClickListener {
                val action = HomeFragmentDirections.actionGlobalSearchResultFragment(ListType.DEALS, DealType.FLIGHTS)
                findNavController().navigate(action)
            }
            hotels.root.setOnClickListener {
                val action = HomeFragmentDirections.actionGlobalSearchResultFragment(ListType.DEALS, DealType.HOTELS)
                findNavController().navigate(action)
            }
            cars.root.setOnClickListener {
                val action = HomeFragmentDirections.actionGlobalSearchResultFragment(ListType.DEALS, DealType.TRANSPORTATIONS)
                findNavController().navigate(action)
            }
            taxi.root.setOnClickListener {
                val action = HomeFragmentDirections.actionGlobalSearchResultFragment(ListType.DEALS, DealType.TRANSPORTATIONS)
                findNavController().navigate(action)
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

    private fun subscribeToObservable() {
        viewModel.dealList.observe(viewLifecycleOwner) { travelList ->
            adapter.submitList(travelList)
        }
    }

    override fun onResume() {
        super.onResume()
        tabPosition = viewModel.getTabPosition()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setTabPosition(tabPosition)
    }

    enum class DealType(val category: String) {
        FLIGHTS("flight"),
        HOTELS("hotel"),
        TRANSPORTATIONS("transportation")
    }
}