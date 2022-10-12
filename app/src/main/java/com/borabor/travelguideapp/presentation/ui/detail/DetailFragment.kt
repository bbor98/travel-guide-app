package com.borabor.travelguideapp.presentation.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentDetailBinding
import com.borabor.travelguideapp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    override val viewModel: DetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<View>(R.id.fade).visibility = View.GONE
        getArgs()
        setupAdapter()
        setupViewClickListeners()
        subscribeToObservable()
    }

    private fun getArgs() {
        val args = arguments?.let { DetailFragmentArgs.fromBundle(it) }
        args?.let { binding.travel = it.travel }
    }

    private fun setupAdapter() {
        ImageAdapter { binding.imagePosition = it }.apply {
            binding.rvImages.adapter = this
            submitList(binding.travel!!.images)
        }
    }

    private fun setupViewClickListeners() {
        binding.apply {
            btBack.setOnClickListener {
                findNavController().navigateUp()
            }

            ivImage.setOnClickListener {
                val action = DetailFragmentDirections.actionDetailFragmentToFullscreenImageFragment(travel!!.images.toTypedArray())
                findNavController().navigate(action)
            }

            btBookmark.setOnClickListener {
                btBookmark.visibility = View.GONE
                pbLoading.visibility = View.VISIBLE
                viewModel.bookmark(travel!!.id, travel!!.isBookmark)
            }
        }
    }

    private fun subscribeToObservable() {
        viewModel.isBookmark.observe(viewLifecycleOwner) { isBookmark ->
            isBookmark?.let {
                binding.apply {
                    travel = travel?.copy(isBookmark = it)
                    pbLoading.visibility = View.GONE
                    btBookmark.visibility = View.VISIBLE
                }
            }
        }

        viewModel.bookmarkState.observe(viewLifecycleOwner) { bookmarkState ->
            bookmarkState?.let {
                if (it.isError) {
                    binding.btBookmark.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE

                    Toast.makeText(requireContext(), getString(R.string.error_bookmark), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.setImagePosition(binding.imagePosition)
    }

    override fun onResume() {
        super.onResume()
        binding.imagePosition = viewModel.imagePosition.value
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<View>(R.id.fade).visibility = View.VISIBLE
    }
}