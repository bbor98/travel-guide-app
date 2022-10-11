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
import com.borabor.travelguideapp.util.calculateDaysBetweenDates
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
    }/*.apply {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvTripPlan.scrollToPosition(0)
            }
        })
    }*/

    private val adapterBookmark = TripPlanAdapter(false, { viewModel.deleteBookmark(it.id) }) { travel ->
        val action = HomeFragmentDirections.actionGlobalDetailFragment(travel)
        findNavController().navigate(action)
    }/*.apply {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvTripPlan.scrollToPosition(0)
            }
        })
    }*/

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

        binding.rvTripPlan.adapter = if (viewModel.tabPosition.value == 0) adapterTrip else adapterBookmark

        setupTabLayout()
        setupBottomSheetDialog()
        subscribeToObservables()

        binding.fabAddTrip.setOnClickListener {
            viewModel.fetchTravelList()
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
                if (tvStartDate.text == getString(R.string.pick_start_date) || tvEndDate.text == getString(R.string.pick_end_date)) {
                    Toast.makeText(requireContext(), getString(R.string.empty_dates), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val schedule = "${tvStartDate.text} - ${tvEndDate.text}"

                if (schedule.calculateDaysBetweenDates() <= 0) {
                    Toast.makeText(requireContext(), getString(R.string.invalid_dates), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                trip = trip.copy(schedule = schedule)

                viewModel.addTrip(trip)
                viewModel.fetchTripList()

                binding.rvTripPlan.scrollToPosition(0)

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
        binding.tlTripPlan.getTabAt(viewModel.tabPosition.value!!)?.select()
        binding.tlTripPlan.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.rvTripPlan.adapter = adapterTrip
                        viewModel.fetchTripList()
                    }
                    1 -> {
                        binding.rvTripPlan.adapter = adapterBookmark
                        viewModel.fetchBookmarkList()
                    }
                }

                binding.rvTripPlan.scrollToPosition(0)
                viewModel.setTabPosition(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun subscribeToObservables() {
        viewModel.tripList.observe(viewLifecycleOwner) { tripList ->
            adapterTrip.submitList(tripList)
        }

        viewModel.travelList.observe(viewLifecycleOwner) { allTravelList ->
            if (this::bottomSheetBinding.isInitialized) {
                setupBottomSheetSpinner(allTravelList)
            }
        }

        viewModel.bookmarkList.observe(viewLifecycleOwner) { bookmarkList ->
            adapterBookmark.submitList(bookmarkList)
        }

        viewModel.bookmarkState.observe(viewLifecycleOwner) { bookmarkState ->
            if (bookmarkState.isError) Toast.makeText(requireContext(), getString(R.string.error_bookmark), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.tabPosition.value == 1) viewModel.fetchBookmarkList()
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