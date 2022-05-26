package com.test.meli.domain.exception

open class DomainException(override val message: String = "") : Throwable(message)
object NotFoundException : DomainException()
object BadRequestException : DomainException()
object InternalErrorException : DomainException()
object UnknownUser : DomainException()
object Unauthorized : DomainException()
data class HttpErrorCode(val code: Int, override val message: String) : DomainException()
