package com.example.lab2.data.model.user_model

import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers


interface UserApi {
    @POST("login/")
    suspend fun loginUser(@Body login_user_data: LoginUserData): Response<AuthCode>

    @POST("register/")
    suspend fun registerUser(@Body register_user_data: RegisterUserData): Response<Unit>

    @POST("users/add_transaction/")
    suspend fun addConversion(
        @Header("Authorization") token: String,
        @Body conversion_request: AddConversionRequest,
    ): Response<Unit>
}

object UserRetrofitInstance {
    private const val BASE_URL = "https://lab1webmaga-production.up.railway.app/exchanger/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}