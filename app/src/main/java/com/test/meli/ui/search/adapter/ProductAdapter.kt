package com.test.meli.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.test.meli.R
import com.test.meli.databinding.ItemProductBinding
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.basicDiffUtil
import com.test.meli.ui.ext.formatCurrency
import com.test.meli.ui.ext.gone
import com.test.meli.ui.ext.setSafeOnClickListener
import com.test.meli.ui.ext.visible
import com.test.meli.ui.util.Condition

class ProductAdapter(val callBack: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var products: List<Product> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = products.size

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context get() = binding.root.context

        init {
            binding.root.setSafeOnClickListener {
                val product = products[adapterPosition]
                callBack.invoke(product)
            }
        }

        fun bind() {
            with(binding) {
                val product = products[adapterPosition]
                if (product.shipping?.freeShipping == true) {
                    txvFreeShipping.visible()
                } else {
                    txvFreeShipping.gone()
                }

                txvCondition.text = when (product.condition) {
                    Condition.New.value -> {
                        txvCondition.visible()
                        context.getString(R.string.condition_new)
                    }
                    Condition.Use.value -> {
                        txvCondition.visible()
                        context.getString(R.string.condition_use)
                    }
                    else -> {
                        txvCondition.gone()
                        String()
                    }
                }
                txvNameProduct.text = product.title
                txvPrice.text = product.price.formatCurrency()
                imvProduct.load(product.thumbnail) {
                    size(500)
                }
                product.seller.eshop?.nickName?.let {
                    txvShopsName.text = context.getString(R.string.sold_by, it)
                    txvShopsName.visible()
                } ?: run {
                    txvShopsName.gone()
                }
            }
        }
    }
}
