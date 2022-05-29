package com.test.meli.di.search

import com.test.meli.data.endpoint.SearchProductApi
import com.test.meli.data.repository.SearchProductRepositoryImpl
import com.test.meli.domain.repository.DomainExceptionRepository
import com.test.meli.domain.repository.SearchProductRepository
import com.test.meli.domain.uc.SearchProductUC
import com.test.meli.ui.search.viewmodel.SearchViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object SearchModule {

    @Provides
    fun searchViewModelProvider(
        searchProductUC: SearchProductUC
    ) = SearchViewModel(searchProductUC)

    @Provides
    @ViewModelScoped
    fun searchProductUCProvider(
        searchProductRepository: SearchProductRepository
    ) = SearchProductUC(searchProductRepository)

    @Provides
    @ViewModelScoped
    fun searchProductRepositoryImplProvider(
        searchProductApi: SearchProductApi,
        domainExceptionRepository: DomainExceptionRepository
    ): SearchProductRepository =
        SearchProductRepositoryImpl(searchProductApi, domainExceptionRepository)

    @Provides
    @ViewModelScoped
    fun searchProductApiProvide(retrofit: Retrofit): SearchProductApi =
        retrofit.create(SearchProductApi::class.java)
}
