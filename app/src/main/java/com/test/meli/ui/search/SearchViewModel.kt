package com.test.meli.ui.search

import androidx.lifecycle.ViewModel
import com.test.meli.domain.uc.SearchProductUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductUC: SearchProductUC
) : ViewModel()
