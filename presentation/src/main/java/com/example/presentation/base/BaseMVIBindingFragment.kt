package com.example.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

import kotlinx.coroutines.flow.Flow


private typealias FragmentViewBindingInflater<VB> = (
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) -> VB

abstract class BaseMVIBindingFragment<I, S, VB : ViewBinding>(private val bindingInflater: FragmentViewBindingInflater<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }


    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

     abstract fun intents(): Flow<I>
     abstract fun updateState(viewState: S)
}