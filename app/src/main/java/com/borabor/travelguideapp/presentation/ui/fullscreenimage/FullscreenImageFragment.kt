package com.borabor.travelguideapp.presentation.ui.fullscreenimage

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.borabor.travelguideapp.R
import com.borabor.travelguideapp.databinding.FragmentFullscreenImageBinding
import com.borabor.travelguideapp.domain.model.Image
import com.borabor.travelguideapp.presentation.base.BaseFragment
import com.borabor.travelguideapp.presentation.ui.detail.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullscreenImageFragment : BaseFragment<FragmentFullscreenImageBinding>(R.layout.fragment_fullscreen_image) {

    override val viewModel: DetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        setupClickListeners()
    }

    private fun getArgs() {
        val args = arguments?.let { FullscreenImageFragmentArgs.fromBundle(it) }
        args?.let { setupViewPager(it.imageList.toList()) }
    }

    private fun setupViewPager(imageList: List<Image>) {
        binding.vpImages.apply {
            adapter = FullscreenImageAdapter { toggleUiVisibility() }.apply { submitList(imageList) }
            setCurrentItem(viewModel.imagePosition.value!!, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    binding.imageNumber = "${position + 1}/${imageList.size}"
                    viewModel.setImagePosition(position)
                }
            })
        }
    }

    private fun toggleUiVisibility() {
        if (binding.isFullscreen == true) showUi() else hideUi()
    }

    private fun hideUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, binding.frameLayout).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        binding.isFullscreen = true
    }

    private fun showUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        WindowInsetsControllerCompat(requireActivity().window, binding.frameLayout).show(WindowInsetsCompat.Type.systemBars())

        binding.isFullscreen = false
    }

    private fun setupClickListeners(){
        binding.btClose.root.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}