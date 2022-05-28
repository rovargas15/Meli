package com.test.meli.data.exception

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.test.meli.domain.exception.BadRequestException
import com.test.meli.domain.exception.DomainException
import com.test.meli.domain.exception.HttpErrorCode
import com.test.meli.domain.exception.InternalErrorException
import com.test.meli.domain.exception.NotFoundException
import com.test.meli.domain.exception.Unauthorized
import com.test.meli.domain.exception.UnknownError
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

object HttpErrors {

    private val moshi = Moshi.Builder().build()
    private val jsonAdapter: JsonAdapter<DomainException> =
        moshi.adapter(DomainException::class.java)

    private val httpErrors = mapOf(
        HttpsURLConnection.HTTP_BAD_REQUEST to BadRequestException,
        HttpsURLConnection.HTTP_NOT_FOUND to NotFoundException,
        HttpsURLConnection.HTTP_INTERNAL_ERROR to InternalErrorException,
        HttpsURLConnection.HTTP_UNAUTHORIZED to Unauthorized
    )

    fun getHttpError(error: HttpException): DomainException {
        return if (httpErrors.containsKey(error.code())) {
            httpErrors.getValue(error.code())
        } else {
            HttpErrorCode(error.code(), getMessage(error).message)
        }
    }

    private fun getMessage(exception: HttpException): DomainException {
        return try {
            var jsonString = exception.response()?.errorBody()?.string()
            if (jsonString.isNullOrEmpty()) jsonString = "{}"
            jsonAdapter.fromJson(jsonString)!!
        } catch (exception_: Exception) {
            UnknownError
        }
    }
}
