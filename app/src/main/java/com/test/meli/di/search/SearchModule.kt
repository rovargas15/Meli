package com.test.meli.di.search

import com.test.meli.data.endpoint.ProductApi
import com.test.meli.data.repository.ProductRepositoryImpl
import com.test.meli.domain.repository.DomainExceptionRepository
import com.test.meli.domain.repository.ProductRepository
import com.test.meli.domain.uc.GetProductByUC
import com.test.meli.ui.search.viewmodel.SearchViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object SearchModule {

    @Provides
    fun searchViewModelProvider(
        getProductByUC: GetProductByUC,
        coroutineDispatcher: CoroutineDispatcher
    ) = SearchViewModel(getProductByUC, coroutineDispatcher)

    @Provides
    @ViewModelScoped
    fun searchProductUCProvider(
        productRepository: ProductRepository
    ) = GetProductByUC(productRepository)

    @Provides
    @ViewModelScoped
    fun searchProductRepositoryImplProvider(
        productApi: ProductApi,
        domainExceptionRepository: DomainExceptionRepository
    ): ProductRepository =
        ProductRepositoryImpl(productApi, domainExceptionRepository)

    @Provides
    @ViewModelScoped
    fun searchProductApiProvide(retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)
}
