package com.test.meli.domain.repository

import com.test.meli.domain.exception.DomainException

interface DomainExceptionRepository {

    fun manageError(error: Throwable): DomainException
}
