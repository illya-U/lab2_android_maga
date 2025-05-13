package com.example.lab2.data.model.user_model


data class LoginUserData(
    val email: String,
    val password: String,
)

data class RegisterUserData(
    val username: String,
    val email: String,
    val password: String,
    val gender: String,
    val birth_date: String
)

data class AuthCode(
    val token: String,
)

data class AddConversionRequest(
    val amount: Double,
    val from_currency: String,
    val to_currency: String
)

class UserRepository {
    private var auth_token: String = ""

    fun get_auth_token(): String = auth_token

    fun set_auth_token(value: String) {
        auth_token = value
    }
}