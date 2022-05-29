package com.test.meli.ui.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.size.Scale
import com.test.meli.R
import com.test.meli.databinding.FragmentDetailsBinding
import com.test.meli.domain.model.Attribute
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.createRow
import com.test.meli.ui.ext.createTextview
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.ext.gone
import com.test.meli.ui.ext.visible
import com.test.meli.ui.search.viewmodel.SearchViewModel
import com.test.meli.ui.util.Condition
import dagger.hilt.android.AndroidEntryPoint

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
        }
        createTable(product.attributes)
    }

    private fun createTable(attributes: List<Attribute>) {

        attributes.forEach { attribute ->
            val tableLayout = TableLayout(context).apply {
                layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val txvTitle = context?.createTextview {
                text = attribute.attributeGroupName
            }

            val titleRow = context?.createRow(txvTitle)
            tableLayout.addView(titleRow)

            val txvValueName = context?.createTextview {
                text = attribute.name
            }

            val rowValue = context?.createRow(txvValueName)

            if (attribute.valueName.isNullOrEmpty()) {
                attribute.values.forEach {
                    if (it.isNotEmpty()) {
                        val txvValue = context?.createTextview {
                            text = it
                        }
                        rowValue?.addView(txvValue)
                        tableLayout.addView(rowValue)
                    }
                }
            } else {
                val txvValue = TextView(context)
                txvValue.text = attribute.valueName
                rowValue?.addView(txvValue)
                tableLayout.addView(rowValue)
            }

            binding.cnlAttributes.addView(tableLayout)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
