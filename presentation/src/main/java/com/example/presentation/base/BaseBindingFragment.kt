package com.example.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

private typealias FragmentVBInflater<VB> = (
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) -> VB


abstract class BaseBindingFragment<VB : ViewBinding>(private val viewBindingInflater: FragmentVBInflater<VB>) : Fragment() {

    private var _viewBinding :VB? = null
    protected val viewBinding :VB get() = _viewBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding=viewBindingInflater.invoke(inflater,container,false)
        return viewBinding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

}