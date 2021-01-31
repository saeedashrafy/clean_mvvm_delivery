package com.example.presentation.features.product.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.market.core.clicks
import com.example.market.core.dispatchers.launchWhenStartedUntilStopped
import com.example.presentation.R
import com.example.presentation.base.BaseBindingFragment
import com.example.presentation.databinding.FragmentFoodsBinding
import com.example.presentation.features.product.details.ProductDetailsActivity
import com.example.presentation.utils.convertToArabic
import com.example.presentation.utils.snack
import com.google.android.material.chip.Chip
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val EXTRA_FOOD = "extra_food"
class FoodsFragment :
        BaseBindingFragment<FragmentFoodsBinding>(FragmentFoodsBinding::inflate) {


    private val viewModel by viewModel<ProductViewModel>()
    private val productAdapter = FoodAdapter()
    private var foodRows = ArrayList<FoodRow>()
    private var selectedCatId: Int = 0
    private var cartSum = 0
    private val categoriesList = ArrayList<Chip>()
    private var linearLayoutManager: LinearLayoutManager? = null
    private val increaseFoodCountChannel =
            Channel<FoodRow.Food>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val decreaseFoodCountChannel =
            Channel<FoodRow.Food>(onBufferOverflow = BufferOverflow.DROP_OLDEST)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindVM(viewModel)
    }

    private fun bindVM(viewModel: ProductViewModel) {


        viewModel.viewState
                .onEach {
                    renderUI(it)
                }
                .launchWhenStartedUntilStopped(this)

        viewModel.singleEvent
                .onEach { handleSingleEvent(it) }
                .launchWhenStartedUntilStopped(this)

        intents()
                .onEach {
                    viewModel.processIntent(it)
                }.launchIn(lifecycleScope)
    }

    private fun handleSingleEvent(it: SingleEvent) {
        when (it) {
            is SingleEvent.GetCategoryProductsError -> viewBinding.root.snack("خطا")
        }
    }

    private fun renderUI(viewState: ViewState) {
        Log.d("TAG", viewState.cartSum.toString())
        if (viewState.foods.isNotEmpty()) {
            viewState.foods.let {
                if (categoriesList.isEmpty()) {
                    val chipGroup = viewBinding.categoryChipGroup
                    val inflater = LayoutInflater.from(chipGroup.context)
                    val children = it.filterIsInstance<FoodRow.Category>()
                            .map { foodRow: FoodRow -> foodRow as FoodRow.Category }.map { category ->
                                val chip =
                                        inflater.inflate(R.layout.row_chip_view, chipGroup, false) as Chip
                                chip.text = category.title
                                chip.tag = category.id
                                chip.id = category.id
                                if (chip.id == selectedCatId)
                                    chip.isChecked = true
                                return@map chip
                            }

                    categoriesList.clear()
                    categoriesList.addAll(children)
                    chipGroup.removeAllViews()
                    for (chip in children) {
                        chipGroup.addView(chip)
                    }
                    linearLayoutManager?.scrollToPositionWithOffset(0, 0)
                }

                foodRows.clear()
                foodRows.addAll(it)
                productAdapter.submitList(it)
            }
        }
        cartSum = viewState.cartSum

        viewBinding.run {
            errorGroup.isVisible = viewState.error != null
            errorMessageTextView.text = viewState.error?.message
            progressBar.isVisible = viewState.isLoading
            " تکمیل خرید (${cartSum.toString().convertToArabic()})".also { fabCart.text = it }
            fabCart.isVisible = cartSum != 0
        }


    }

    private fun setupViews() {

        viewBinding.fabCart.setOnClickListener {
            findNavController().navigate(R.id.cardFragment)
        }
        productAdapter.setCellClickListener(object : FoodCellClickListener {
            override fun clickedButtonAdd(foodRow: FoodRow.Food) {
                increaseFoodCountChannel.offer(foodRow as FoodRow.Food)
            }

            override fun clickedButtonRemove(foodRow: FoodRow.Food) {
                decreaseFoodCountChannel.offer(foodRow as FoodRow.Food)
            }

            override fun onClickRow(foodRow: FoodRow.Food) {
                val intent = Intent(activity, ProductDetailsActivity::class.java)
                intent.putExtra(EXTRA_FOOD,foodRow)
                startActivity(intent)
            }
        })


        linearLayoutManager = LinearLayoutManager(activity)
        viewBinding.productRecycler.apply {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setHasFixedSize(true)
            adapter = productAdapter
            var itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(ContextCompat.getDrawable(this.context, R.drawable.divider)!!)
            addItemDecoration(itemDecoration)
            layoutManager = linearLayoutManager

            addOnScrollListener(object : RecyclerView.OnScrollListener() {


                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    viewBinding.fabCart.isVisible = dy <= 0 && cartSum != 0

                    linearLayoutManager?.let { lm ->
                        val index = lm.findFirstVisibleItemPosition()
                        foodRows.isNotEmpty().let {

                            if ((foodRows.size - 1) >= index) {
                                foodRows[index].let { row ->

                                    when (row) {
                                        is FoodRow.Food -> {
                                            val food = row as FoodRow.Food
                                            if (selectedCatId != food.categoryId) {
                                                selectedCatId = food.categoryId
                                                val selectedChip =
                                                        categoriesList.find { chip -> chip.tag == selectedCatId }
                                                viewBinding.categoryChipGroup.check(selectedChip!!.id)
                                                val hsvWidth = viewBinding.categoryScroll.width

                                                val chipLeft = selectedChip.right
                                                val chipWidth = selectedChip.width
                                                val offset = chipLeft - chipWidth / 2 - hsvWidth / 2
                                                viewBinding.categoryScroll.smoothScrollTo(offset, 0)

                                            }
                                        }
                                        is FoodRow.Category -> {
                                            val category = row as FoodRow.Category
                                            if (selectedCatId != category.id) {
                                                selectedCatId = category.id
                                                val selectedChip =
                                                        categoriesList.find { chip -> chip.tag == selectedCatId }

                                                viewBinding.categoryChipGroup.check(selectedChip!!.id)

                                                val hsvWidth = viewBinding.categoryScroll.width

                                                val chipLeft = selectedChip.right
                                                val chipWidth = selectedChip.width
                                                val offset = chipLeft - chipWidth / 2 - hsvWidth / 2
                                                viewBinding.categoryScroll.smoothScrollTo(offset, 0)
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            })

            viewBinding.categoryChipGroup.setOnCheckedChangeListener { group, checkedId ->
                if (selectedCatId != checkedId) {
                    selectedCatId = checkedId
                    val category =
                            foodRows.find { foodRow -> (foodRow is FoodRow.Category) && foodRow.id == checkedId }
                    val po = foodRows.indexOf(category)

                    if (po != -1) {
                        linearLayoutManager?.scrollToPositionWithOffset(po, 0)
                        selectedCatId = (category as FoodRow.Category).id

                    }


                }
            }


        }


    }


    private fun intents(): Flow<ViewIntent> = merge(
            flowOf(ViewIntent.Initial),
            increaseFoodCountChannel.consumeAsFlow()
                    .map { ViewIntent.IncreaseCount(it as FoodRow.Food) },
            decreaseFoodCountChannel.consumeAsFlow().map { ViewIntent.DecreaseCount(it as FoodRow.Food) },
            viewBinding.retryButton.clicks().map { ViewIntent.Retry }
            //   viewbinding.retryButton.clicks().map { ViewIntent.Retry }

    )


}