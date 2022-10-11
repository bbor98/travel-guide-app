package com.borabor.travelguideapp.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: DealAdapter

    private var tabPosition = 0

    private var snackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater).apply {
            lifecycleOwner = this@HomeFragment
            viewModel = this@HomeFragment.viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBannerButtons()
        setupTabLayout()
        setupAdapter()
        subscribeToObservables()

        binding.apiResponseState.btRetry.setOnClickListener {
            viewModel.retry()
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

    override fun onPause() {
        super.onPause()
        snackbar?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAB_POSITION = "tab_position"
    }
}