package com.test.meli.domain.exception

import com.squareup.moshi.JsonDataException
import java.net.ConnectException
import java.net.SocketTimeoutException

class CommonErrors {

    fun manageException(throwable: Throwable): DomainException {
        return manageJavaErrors(throwable)
    }

    private fun manageJavaErrors(throwable: Throwable): DomainException {
        return when (throwable) {
            is SocketTimeoutException -> DomainException.TimeOutException
            is ConnectException -> DomainException.InternalErrorException
            else -> manageParsingExceptions(throwable)
        }
    }

    private fun manageParsingExceptions(throwable: Throwable): DomainException {
        return when (throwable) {
            is JsonDataException -> DomainException.ParseException
            else -> manageOtherException(throwable)
        }
    }

    private fun manageOtherException(throwable: Throwable): DomainException {
        return when (throwable) {
            is DomainException.NoConnectivityException -> DomainException.NoConnectivityDomainException
            else -> DomainException.UnknownError
        }
    }
}
