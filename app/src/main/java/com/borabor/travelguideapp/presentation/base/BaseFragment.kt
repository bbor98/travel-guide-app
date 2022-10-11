package com.borabor.travelguideapp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutId: Int) : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    abstract val viewModel: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<B>(inflater, layoutId, container, false).apply {
        _binding = this
        lifecycleOwner = viewLifecycleOwner
        setVariable(BR.viewModel, viewModel)
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}