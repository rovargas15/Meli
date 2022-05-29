package com.test.meli.di.network

import android.app.Application
import com.squareup.moshi.Moshi
import com.test.meli.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun httpInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /**
     * An own OkHttpClient for the main flow of the app
     * @param interceptor it is an interceptor to verify log a request
     */
    @Provides
    @Singleton
    fun okHttpProvider(
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .dispatcher(Dispatcher().apply { maxRequests = MAX_REQUEST })
            .addInterceptor(interceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    /**
     * An own OkHttpClient for the main flow of the app
     * @param client
     * @param context
     */
    @Provides
    @Singleton
    fun retrofitProvider(
        client: OkHttpClient,
        context: Application
    ): Retrofit {
        val urlBase = context.getString(R.string.base_url)
        val moshi = Moshi.Builder()
            .build()
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .baseUrl(urlBase)
            .build()
    }
}

private const val MAX_REQUEST = 1
private const val TIMEOUT = 20L
