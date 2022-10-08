package com.borabor.travelguideapp.presentation.ui.trip

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentTripBinding
import com.borabor.travelguideapp.databinding.LayoutBottomSheetBinding
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.presentation.ui.home.HomeFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TripFragment : Fragment() {

    private var _binding: FragmentTripBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripViewModel by viewModels()

    private val adapterTrip = TripPlanAdapter(true, { viewModel.removeTrip(it) }) { travel ->
        val action = HomeFragmentDirections.actionGlobalDetailFragment(travel)
        findNavController().navigate(action)
    }

    private val adapterBookmark = TripPlanAdapter(false, { viewModel.deleteBookmark(it.id) }) { travel ->
        val action = HomeFragmentDirections.actionGlobalDetailFragment(travel)
        findNavController().navigate(action)
    }

    private var tabPosition = 0

    private var snackbar: Snackbar? = null

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: LayoutBottomSheetBinding

    private lateinit var trip: Travel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTripBinding.inflate(inflater).apply {
            lifecycleOwner = this@TripFragment
            viewModel = this@TripFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        binding.rvTripPlan.adapter = adapterTrip
        setupBottomSheetDialog()
        subscribeToObservables()

        binding.fabAddTrip.setOnClickListener {
            viewModel.fetchTravelList(false)
            bottomSheetDialog.show()
        }
    }

    private fun setupBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

        bottomSheetBinding = DataBindingUtil.inflate<LayoutBottomSheetBinding>(
            layoutInflater,
            R.layout.layout_bottom_sheet,
            view?.findViewById(R.id.container),
            false
        ).apply {
            bottomSheetDialog.setContentView(root)

            tvStartDate.setOnClickListener {
                setupDatePickedDialog(it as TextView)
            }

            tvEndDate.setOnClickListener {
                setupDatePickedDialog(it as TextView)
            }

            btAddTrip.setOnClickListener {
                trip = trip.copy(
                    dateCreated = System.currentTimeMillis(),
                    schedule = "${tvStartDate.text} - ${tvEndDate.text}"
                )

                viewModel.addTrip(trip)
                bottomSheetDialog.dismiss()
            }
        }
    }

    private fun setupDatePickedDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, y, m, d ->
            calendar.set(Calendar.YEAR, y)
            calendar.set(Calendar.MONTH, m)
            calendar.set(Calendar.DAY_OF_MONTH, d)

            textView.text = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(calendar.time)
        }, year, month, day).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    private fun setupBottomSheetSpinner(list: List<Travel>) {
        bottomSheetBinding.apply {
            spDestination.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, list.map { it.title })
            spDestination.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    bottomSheetBinding.imageUrl = list[position].images[0].url
                    trip = list[position]
                    view?.let {
                        (it as TextView).apply {
                            text = trip.title
                            setTextColor(Color.BLACK)
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun setupTabLayout() {
        binding.tlTripPlan.getTabAt(tabPosition)?.select()
        binding.tlTripPlan.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.fabAddTrip.visibility = View.VISIBLE
                        binding.apiResponseState.root.visibility = View.GONE
                        binding.rvTripPlan.adapter = adapterTrip
                        viewModel.fetchTripList()
                    }
                    1 -> {
                        binding.fabAddTrip.visibility = View.GONE
                        binding.apiResponseState.root.visibility = View.VISIBLE
                        binding.rvTripPlan.adapter = adapterBookmark
                        viewModel.fetchTravelList(true)
                    }
                }

                binding.rvTripPlan.scrollToPosition(0)
                tabPosition = tab?.position ?: 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun subscribeToObservables() {
        viewModel.tripList.observe(viewLifecycleOwner) { list ->
            adapterTrip.submitList(list)
        }

        viewModel.travelList.observe(viewLifecycleOwner) { list ->
            adapterBookmark.submitList(list)
            if (this::bottomSheetBinding.isInitialized) {
                setupBottomSheetSpinner(list)
            }
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