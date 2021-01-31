package com.example.presentation.features.cart


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.market.core.clicks
import com.example.market.core.dispatchers.launchWhenStartedUntilStopped
import com.example.presentation.R
import com.example.presentation.base.BaseBindingFragment
import com.example.presentation.databinding.FragmentCartBinding
import com.example.presentation.utils.convertToArabic
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CartFragment : BaseBindingFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private val viewModel by viewModel<CartViewModel>()
    private val cartAdapter = CartAdapter()
    private var linearLayoutManager: LinearLayoutManager? = null
    private val increaseCartItemCountChannel = Channel<CartRow>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val decreaseCartItemCountChannel = Channel<CartRow>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        bindViewModel(viewModel)
    }

    private fun setupViews() {
        linearLayoutManager = LinearLayoutManager(activity)
        viewBinding.recycleCart.apply {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setHasFixedSize(true)
            adapter = cartAdapter
            var itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(ContextCompat.getDrawable(this.context, R.drawable.divider)!!)
            addItemDecoration(itemDecoration)
            layoutManager = linearLayoutManager
        }

        cartAdapter.setCartCellClickListener(object : CartAdapter.CartCellClickListener {
            override fun clickedButtonAdd(cartRow: CartRow) {
                increaseCartItemCountChannel.offer(cartRow)
            }

            override fun clickedButtonRemove(cartRow: CartRow) {
                decreaseCartItemCountChannel.offer(cartRow)
            }
        })


    }

    private fun bindViewModel(viewModel: CartViewModel) {

        viewModel.viewState
                .onEach { renderUI(it) }
                .launchWhenStartedUntilStopped(this)

        intents().onEach {
            viewModel.processIntent(it)
        }.launchIn(lifecycleScope)
    }

    private fun renderUI(viewState: ViewState) {
        Log.d("TAG", viewState.toString())
        if (viewState.rows != null && viewState.rows.isNotEmpty())
            cartAdapter.submitList(viewState.rows)


        viewState.factor?.let {
            viewBinding.containerFactor.textViewTotalPrice.text = "${it.amount.toString().convertToArabic()} تـومان"
            viewBinding.containerFactor.textViewCartGrossAmount.text = "${it.grossAmount.toString().convertToArabic()} تـومان"
            viewBinding.containerFactor.textViewTaxPrice.text = "${it.tax.toString().convertToArabic()} تـومان"
            viewBinding.containerFactor.textViewPackagingPrice.text = "${it.packagingPrice.toString().convertToArabic()} تـومان"
        }

        viewBinding.run {
            errorGroup.isVisible = viewState.error != null && !viewState.isLoading
            viewBinding.containerFactor.root.isVisible = viewState.factor != null
            errorMessageTextView.text = viewState.error?.message
            progressBar.isVisible = viewState.isLoading
            recycleCart.isVisible = !viewState.rows.isNullOrEmpty()
            emptyMessageTextView.isVisible = viewState.rows.isNullOrEmpty() && viewState.error == null && !viewState.isLoading
        }
    }

    private fun intents(): Flow<ViewIntent> {
        return merge(
                flowOf(ViewIntent.Init), increaseCartItemCountChannel.consumeAsFlow().map { ViewIntent.IncreaseCount(it) },
                decreaseCartItemCountChannel.consumeAsFlow().map { ViewIntent.DecreaseCount(it) },
                viewBinding.retryButton.clicks().map { ViewIntent.Refresh }
        )
    }
}