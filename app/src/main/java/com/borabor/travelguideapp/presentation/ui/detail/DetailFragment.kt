package com.borabor.travelguideapp.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentDetailBinding
import com.borabor.travelguideapp.util.hideBottomNav
import com.borabor.travelguideapp.util.showBottomNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater)
        requireActivity().findViewById<View>(R.id.fade).visibility = View.GONE
        hideBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                val action = DetailFragmentDirections.actionDetailFragmentToFullscreenImageFragment(travel!!.images.toTypedArray(), imagePosition ?: 0)
                findNavController().navigate(action)
            }

            btBookmark.setOnClickListener {
                binding.btBookmark.visibility = View.GONE
                binding.pbLoading.visibility = View.VISIBLE
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
            if (bookmarkState.isError) {
                binding.btBookmark.visibility = View.VISIBLE
                binding.pbLoading.visibility = View.GONE

                Toast.makeText(requireContext(), getString(R.string.error_bookmark), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.setImagePosition(binding.imagePosition)
    }

    override fun onResume() {
        super.onResume()
        binding.imagePosition = viewModel.getImagePosition()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        showBottomNav()
    }
}