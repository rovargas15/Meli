package com.test.meli.ui.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.meli.R
import com.test.meli.databinding.FragmentSearchBinding
import com.test.meli.domain.model.Product
import com.test.meli.ui.ext.gone
import com.test.meli.ui.ext.onClickSearchButton
import com.test.meli.ui.ext.visible
import com.test.meli.ui.search.adapter.ProductAdapter
import com.test.meli.ui.search.state.SearchState
import com.test.meli.ui.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val productAdapter by lazy { ProductAdapter(::selectProduct) }
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        initListener()
        initSubscribe()
    }

    private fun initSubscribe() {
        searchViewModel.productLiveData.observe(viewLifecycleOwner, ::handleSearchState)
    }

    private fun handleSearchState(state: SearchState) {
        with(binding) {
            when (state) {
                is SearchState.Loading -> {
                    lavLoader.visible()
                    rcvProduct.gone()
                }
                is SearchState.Success -> {
                    rcvProduct.visible()
                    productAdapter.products = state.responseQuery.products
                    lavLoader.gone()
                }
                is SearchState.Error -> {
                    lavLoader.gone()
                }
            }
        }
    }

    private fun bind() {
        with(binding) {
            rcvProduct.adapter = productAdapter
            val manager = rcvProduct.layoutManager as GridLayoutManager
            manager.spanCount = 2
        }
    }

    private fun initListener() {
        with(binding) {
            etSearch.onClickSearchButton {
                etSearch.text?.toString()?.let { query ->
                    searchViewModel.searchProduct(query)
                }
            }
        }
    }

    private fun selectProduct(product: Product) {
        findNavController().navigate(R.id.action_SearchFragment_to_DetailsFragment)
        searchViewModel.selectProductDetails(product)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
