package com.example.presentation.features.product.details

import android.os.Bundle
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.market.core.dispatchers.launchWhenStartedUntilStopped
import com.example.presentation.base.BaseBindingActivity
import com.example.presentation.databinding.ActivityProductDetailsBinding
import com.example.presentation.features.product.list.EXTRA_FOOD
import com.example.presentation.features.product.list.FoodRow
import com.example.presentation.utils.AppGlide
import com.example.presentation.utils.convertToArabic
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsActivity : BaseBindingActivity<ActivityProductDetailsBinding>(ActivityProductDetailsBinding::inflate) {

    private val viewModel by viewModel<ProductDetailsViewModel>()
     var food: FoodRow.Food? = null
    private val increaseFoodCountChannel =
            Channel<FoodRow.Food>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val decreaseFoodCountChannel =
            Channel<FoodRow.Food>(onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        food = intent.getSerializableExtra(EXTRA_FOOD) as FoodRow.Food
        setupViews(food)
        bindViewModel(viewModel)

    }

    private fun setupViews(food: FoodRow.Food?) {
        setSupportActionBar(viewbinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        food?.let {
            viewbinding.imageButtonAdd.setOnClickListener {
                increaseFoodCountChannel.offer(food)
            }
            viewbinding.imageButtonRemove.setOnClickListener {
                decreaseFoodCountChannel.offer(food)
            }
            viewbinding.buttonAdd.setOnClickListener {
                increaseFoodCountChannel.offer(food)
            }

            val imagePath = food.image.replace("#SIZEOFIMAGE#", "560x350")
            AppGlide.with(this.applicationContext)
                    .load(imagePath)
                    .into(viewbinding.imageHeader)
            viewbinding.textViewFoodTitle.text = food.title
            viewbinding.textViewFoodIngredient.text = food.ingredient
            viewbinding.textViewFoodPrice.text = "${food.price.toString().convertToArabic()} تومان "
        }

    }

    private fun bindViewModel(viewModel: ProductDetailsViewModel) {

        intents().onEach {
            viewModel.processIntent(it)
        }
                .launchIn(lifecycleScope)

        viewModel.stateFlow.onEach {
            renderUI(it)
        }.launchWhenStartedUntilStopped(this)
    }

    private fun renderUI(viewState: ViewState) {

        viewState.cartFood?.apply {
            viewbinding.imageButtonAdd.isVisible= count != 0
            viewbinding.imageButtonRemove.isVisible= count != 0
            viewbinding.buttonAdd.visibility = if(count == 0) VISIBLE else INVISIBLE
            viewbinding.textViewProductCount.text = count.toString().convertToArabic()

        }
        food = viewState.cartFood?.count?.let { food?.copy(count = it) }

    }

    override fun configureToolbar() {

    }

    private fun intents(): Flow<ViewIntent> {
        return merge(flowOf(ViewIntent.Init(food)),
                increaseFoodCountChannel.consumeAsFlow().map { ViewIntent.Increment(it) },
                decreaseFoodCountChannel.consumeAsFlow().map { ViewIntent.Decrement(it) })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}