package com.test.meli.domain.exception

import java.io.IOException

sealed class DomainException(override val message: String = "") : Throwable(message) {
    object NotFoundException : DomainException()
    object BadRequestException : DomainException()
    object InternalErrorException : DomainException()
    object UnknownError : DomainException()
    object Unauthorized : DomainException()
    object TimeOutException : DomainException()
    object ParseException : DomainException()
    object NoConnectivityException : IOException()
    object NoConnectivityDomainException : DomainException()
    data class HttpErrorCode(val code: Int, override val message: String) : DomainException()
}
