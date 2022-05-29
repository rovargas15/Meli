package com.test.meli.data.repository

import com.test.meli.data.exception.HttpErrors
import com.test.meli.domain.exception.CommonErrors
import com.test.meli.domain.exception.DomainException
import com.test.meli.domain.repository.DomainExceptionRepository
import retrofit2.HttpException

class DomainExceptionRepositoryImpl(
    private val commonErrors: CommonErrors,
    private val httpErrors: HttpErrors
) : DomainExceptionRepository {

    override fun manageError(error: Throwable): DomainException {
        return if (error is HttpException) {
            httpErrors.getHttpError(error)
        } else {
            commonErrors.manageException(error)
        }
    }
}
