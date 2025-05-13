package com.example.lab2.data.model.exchanger_model

import io.github.cdimascio.dotenv.dotenv

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Body

// Retrofit API Interface
interface ExchangerApi {
    @GET("api/v1/currency/list")
    @Headers(
        "x-app-version: 1.0.0",
        "x-apihub-host: Currency-Converter.allthingsdev.co",
        "x-apihub-endpoint: f7a950b7-e795-4241-b4ab-1c646fcabadc"
    )
    suspend fun getCurrencyList(): CurrencyResponse

    @POST("api/v1/currency/conversion")
    @Headers(
        "x-app-version: 1.0.0",
        "x-apihub-host: Currency-Converter.allthingsdev.co",
        "x-apihub-endpoint: a0a275dc-a8ba-4d34-bb29-2787556829d2",
        "Content-Type: application/json"
    )
    suspend fun convertCurrency(@Body conversionRequest: ConversionRequest): ConversionResponse
}

// Retrofit Client Singleton
object ExchangerRetrofitInstance {
    private const val BASE_URL = "https://Currency-Converter.proxy-production.allthingsdev.co/"

    // Load API key from .env
    private val dotenv = dotenv {
        directory = "/assets"
        filename = "env" // instead of '.env', use 'env'
        ignoreIfMissing = false // Throw an error if .env is missing
    }

    private val apiHubKey: String by lazy {
        dotenv["API_HUB_KEY"] ?: throw IllegalStateException("API_HUB_KEY not found in .env")
    }

    // Custom Interceptor to add API key to headers
    private val apiKeyInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val updatedRequest = originalRequest.newBuilder()
                .addHeader("x-apihub-key", apiHubKey) // Add the API key
                .build()
            return chain.proceed(updatedRequest)
        }
    }

    // Create OkHttpClient with Interceptor
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor) // Attach the interceptor
            .build()
    }

    // Build Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ExchangerApi by lazy {
        retrofit.create(ExchangerApi::class.java)
    }
}