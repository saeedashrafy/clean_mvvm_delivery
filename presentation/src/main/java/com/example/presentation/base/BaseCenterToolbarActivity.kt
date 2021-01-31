package com.example.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.example.presentation.R

import kotlinx.coroutines.flow.Flow
import org.koin.android.ext.android.inject


abstract class BaseCenterToolbarActivity<I, S, VB : ViewBinding>(private val bindingInflater: ActivityViewBindingInflater<VB>) : AppCompatActivity() {


    protected lateinit var binding: VB
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        configureToolbar()
    }

    private fun configureToolbar() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        // changes made here, try to find if getSupportActionBar() is null or not after setting it - setSupportActionBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    abstract fun intents(): Flow<I>
    abstract fun updateState(viewState: S)


}