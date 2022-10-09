package com.borabor.travelguideapp.presentation.ui.fullscreenimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.borabor.travelguideapp.databinding.FragmentFullscreenImageBinding
import com.borabor.travelguideapp.domain.model.Image
import com.borabor.travelguideapp.util.hideBottomNav

class FullscreenImageFragment : Fragment() {

    private var _binding: FragmentFullscreenImageBinding? = null
    private val binding get() = _binding!!

    private var isFullscreen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFullscreenImageBinding.inflate(inflater)
        hideBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var imageList = emptyList<Image>()
        var position = 0

        val args = arguments?.let { FullscreenImageFragmentArgs.fromBundle(it) }
        args?.let {
            imageList = it.imageList.toList()
            position = it.imagePosition
        }

        val totalImageCount = imageList.size

        binding.vpImages.apply {
            adapter = FullscreenImageAdapter { toggleUiVisibility() }.apply { submitList(imageList) }
            setCurrentItem(position, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    binding.imageNumber = "${position + 1}/$totalImageCount"
                }
            })
        }

        binding.btClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun toggleUiVisibility() {
        if (isFullscreen) showUi() else hideUi()
    }

    private fun hideUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, binding.frameLayout).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        binding.isFullscreen = true
        isFullscreen = true
    }

    private fun showUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        WindowInsetsControllerCompat(requireActivity().window, binding.frameLayout).show(WindowInsetsCompat.Type.systemBars())

        binding.isFullscreen = false
        isFullscreen = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}