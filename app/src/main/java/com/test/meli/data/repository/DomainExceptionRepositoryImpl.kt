package com.test.meli.data.repository

import com.test.meli.data.exception.HttpErrors.getHttpError
import com.test.meli.domain.exception.CommonErrors
import com.test.meli.domain.exception.DomainException
import com.test.meli.domain.repository.DomainExceptionRepository
import retrofit2.HttpException

class DomainExceptionRepositoryImpl(
    private val commonErrors: CommonErrors
) : DomainExceptionRepository {

    override fun manageError(error: Throwable): DomainException {
        return if (error is HttpException) {
            getHttpError(error)
        } else {
            commonErrors.manageException(error)
        }
    }
}
