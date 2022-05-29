package com.test.meli.di.domain

import com.test.meli.data.exception.HttpErrors
import com.test.meli.data.repository.DomainExceptionRepositoryImpl
import com.test.meli.domain.exception.CommonErrors
import com.test.meli.domain.repository.DomainExceptionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ExceptionModule {

    @Provides
    @ViewModelScoped
    fun commonErrorsProvider() = CommonErrors()

    @Provides
    @ViewModelScoped
    fun httpErrorsProvider() = HttpErrors()

    @Provides
    @ViewModelScoped
    fun domainExceptionRepositoryProvider(
        commonErrors: CommonErrors,
        httpErrors: HttpErrors
    ): DomainExceptionRepository =
        DomainExceptionRepositoryImpl(commonErrors, httpErrors)
}
