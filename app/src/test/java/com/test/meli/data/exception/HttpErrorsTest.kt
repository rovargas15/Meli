package com.test.meli.data.exception

import com.test.meli.domain.exception.BadRequestException
import com.test.meli.domain.exception.InternalErrorException
import com.test.meli.domain.exception.NotFoundException
import com.test.meli.domain.exception.Unauthorized
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException

class HttpErrorsTest {

    private val exception: HttpException = mockk(relaxed = true)
    private val httpErrors = HttpErrors()

    @Test
    fun giveExceptionWhenGetHttpErrorThenReturnBadRequestException() {
        // Give
        every { exception.code() } answers { 400 }

        // When
        val result = httpErrors.getHttpError(exception)

        // Then
        assertEquals(result, BadRequestException)

        verify(exactly = 2) { exception.code() }
        confirmVerified(exception)
    }

    @Test
    fun giveExceptionWhenGetHttpErrorThenReturnNotFoundException() {
        // Give
        every { exception.code() } answers { 404 }

        // When
        val result = httpErrors.getHttpError(exception)

        // Then
        assertEquals(result, NotFoundException)

        verify(exactly = 2) { exception.code() }
        confirmVerified(exception)
    }

    @Test
    fun giveExceptionWhenGetHttpErrorThenReturnInternalErrorException() {
        // Give
        every { exception.code() } answers { 500 }

        // When
        val result = httpErrors.getHttpError(exception)

        // Then
        assertEquals(result, InternalErrorException)

        verify(exactly = 2) { exception.code() }
        confirmVerified(exception)
    }

    @Test
    fun giveExceptionWhenGetHttpErrorThenReturnUnauthorized() {
        // Give
        every { exception.code() } answers { 401 }

        // When
        val result = httpErrors.getHttpError(exception)

        // Then
        assertEquals(result, Unauthorized)

        verify(exactly = 2) { exception.code() }
        confirmVerified(exception)
    }
}
