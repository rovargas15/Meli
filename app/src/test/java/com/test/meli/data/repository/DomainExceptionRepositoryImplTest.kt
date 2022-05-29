package com.test.meli.data.repository

import com.test.meli.data.exception.HttpErrors
import com.test.meli.domain.exception.BadRequestException
import com.test.meli.domain.exception.CommonErrors
import com.test.meli.domain.exception.InternalErrorException
import com.test.meli.domain.exception.NotFoundException
import com.test.meli.domain.exception.ParseException
import com.test.meli.domain.exception.TimeOutException
import com.test.meli.domain.exception.Unauthorized
import com.test.meli.domain.repository.DomainExceptionRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class DomainExceptionRepositoryImplTest {

    private val commonErrors: CommonErrors = mockk(relaxed = true)
    private val exceptionLogin: DomainExceptionRepository =
        DomainExceptionRepositoryImpl(commonErrors, HttpErrors())

    @Test
    fun giveExceptionWhenGetHttpErrorThenReturnBadRequestException() {
        // Give
        val exception: HttpException = mockk()
        every { exception.code() } answers { 400 }

        // When
        val domainException = exceptionLogin.manageError(exception)

        // Then
        assertEquals(domainException, BadRequestException)

        verify(exactly = 2) { exception.code() }
        confirmVerified(exception, commonErrors)
    }

    @Test
    fun giveExceptionWhenGetHttpErrorThenReturnNotFoundException() {
        // Give
        val exception: HttpException = mockk()
        every { exception.code() } answers { 404 }

        // When
        val domainException = exceptionLogin.manageError(exception)

        // Then
        assertEquals(domainException, NotFoundException)

        verify(exactly = 2) { exception.code() }
        confirmVerified(exception, commonErrors)
    }

    @Test
    fun giveExceptionWhenGetHttpErrorReturnInternalErrorException() {
        // Give
        val exception: HttpException = mockk()
        every { exception.code() } answers { 500 }

        // When
        val domainException = exceptionLogin.manageError(exception)

        // Then
        assertEquals(domainException, InternalErrorException)

        verify(exactly = 2) { exception.code() }
        confirmVerified(exception, commonErrors)
    }

    @Test
    fun giveExceptionWhenGetHttpErrorThenReturnUnauthorized() {
        // Give
        val exception: HttpException = mockk()
        every { exception.code() } answers { 401 }

        // When
        val domainException = exceptionLogin.manageError(exception)

        // Then
        assertEquals(domainException, Unauthorized)

        verify(exactly = 2) { exception.code() }
        confirmVerified(exception, commonErrors)
    }

    @Test
    fun giveExceptionWhenManageExceptionThenReturnTimeOutException() {
        // Give
        val exception = SocketTimeoutException()
        every { commonErrors.manageException(exception) } returns TimeOutException

        // When
        val domainException = exceptionLogin.manageError(exception)

        // Then
        assertEquals(domainException, TimeOutException)

        verify(exactly = 1) { commonErrors.manageException(exception) }
        confirmVerified(commonErrors)
    }

    @Test
    fun giveExceptionWhenManageExceptionThenReturnInternalErrorException() {
        // Give
        val exception = ConnectException()
        every { commonErrors.manageException(exception) } returns InternalErrorException

        // When
        val domainException = exceptionLogin.manageError(exception)

        // Then
        assertEquals(domainException, InternalErrorException)

        verify(exactly = 1) { commonErrors.manageException(exception) }
        confirmVerified(commonErrors)
    }

    @Test
    fun giveExceptionWhenManageExceptionThenReturnParseException() {
        // Give
        val exception = ConnectException()
        every { commonErrors.manageException(exception) } returns ParseException

        // When
        val domainException = exceptionLogin.manageError(exception)

        // Then
        assertEquals(domainException, ParseException)

        verify(exactly = 1) { commonErrors.manageException(exception) }
        confirmVerified(commonErrors)
    }
}
