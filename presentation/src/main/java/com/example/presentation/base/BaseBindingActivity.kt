package com.example.presentation.base


import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


public typealias ActivityViewBindingInflater<VB> = (
    inflater: LayoutInflater
) -> VB

abstract class BaseBindingActivity<VB : ViewBinding>(private val bindingInflater: ActivityViewBindingInflater<VB>) : AppCompatActivity() {


    protected lateinit var viewbinding: VB

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewbinding = bindingInflater.invoke(layoutInflater)
        setContentView(viewbinding.root)
        configureToolbar()

    }

    abstract fun configureToolbar()


}