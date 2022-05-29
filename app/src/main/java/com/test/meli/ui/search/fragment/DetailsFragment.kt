package com.test.meli.ui.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.size.Scale
import com.test.meli.R
import com.test.meli.databinding.FragmentDetailsBinding
import com.test.meli.domain.model.Attribute
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.createRow
import com.test.meli.ui.ext.createTable
import com.test.meli.ui.ext.createTextview
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.ext.gone
import com.test.meli.ui.ext.visible
import com.test.meli.ui.search.viewmodel.SearchViewModel
import com.test.meli.ui.util.Condition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSubscribe()
        binding.tbDetailProduct.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initSubscribe() {
        searchViewModel.productDetailLiveData.observe(viewLifecycleOwner, ::handleDetailProduct)
    }

    private fun handleDetailProduct(product: Product) {
        with(binding) {
            if (product.shipping?.freeShipping == true) {
                txvDetailFreeShipping.visible()
            } else {
                txvDetailFreeShipping.gone()
            }

            txvDetailCondition.text = when (product.condition) {
                Condition.New.value -> {
                    txvDetailCondition.visible()
                    getString(R.string.condition_new)
                }
                Condition.Use.value -> {
                    txvDetailCondition.visible()
                    getString(R.string.condition_use)
                }
                else -> {
                    txvDetailCondition.gone()
                    String()
                }
            }
            txvProductTitle.text = product.title
            txvDetailProductPrice.text = product.price.formatCurrency()
            imvDetailProduct.load(product.thumbnail) {
                scale(Scale.FILL)
                size(800)
            }
            txvLocationSeller.text = getString(R.string.location_shop, product.address?.cityName)
            txvReputationSeller.text = getString(
                R.string.reputation_seller,
                product.seller.sellerReputation?.powerSellerStatus
            )
            txvNameShop.text = getString(R.string.sold_by, product.seller.eshop?.nickName)
        }
        createTable(product.attributes)
    }

    private fun createTable(attributes: List<Attribute>) =
        lifecycleScope.launch(context = Dispatchers.IO) {
            val tableLayout = context?.createTable()
            attributes.forEachIndexed { index, attribute ->
                val txvValueName = context?.createTextview {
                    text = attribute.name
                }

                val rowValue = context?.createRow(txvValueName) {
                    if (index.mod(NUMBER_PAR) == 0) {
                        setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                    }
                }
                val txvValue = context?.createTextview()

                if (attribute.valueName.isNullOrEmpty()) {
                    attribute.values.forEach {
                        if (it.isNotEmpty()) {
                            txvValue?.text = it
                        }
                    }
                } else {
                    txvValue?.text = attribute.valueName
                }
                rowValue?.addView(txvValue)
                tableLayout?.addView(rowValue)
            }
            binding.lnlAttributes.addView(tableLayout)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

const val NUMBER_PAR = 2
