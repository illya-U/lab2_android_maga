package com.example.lab2.data.model.user_model
import com.example.lab2.view.view_model.Gender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


object GenderToBackendNameMapper {

    private val genderToString = mapOf(
        Gender.M to "M",
        Gender.F to "F",
        Gender.O to "O",
        Gender.Undefined to "Undefined"
    )

    fun toString(gender: Gender): String {
        return genderToString[gender] ?: "Undefined"
    }
}



class UserModel @Inject constructor() {
    private val user_api: UserApi = UserRetrofitInstance.api
    private val userRepository = UserRepository()

    private val _loginStateFlow = MutableStateFlow<Result<Unit>?>(null)
    val loginStateFlow: StateFlow<Result<Unit>?> = _loginStateFlow

    private val _registerStateFlow = MutableStateFlow<Result<Unit>?>(null)
    val registerStateFlow: StateFlow<Result<Unit>?> = _registerStateFlow

    suspend fun loginUser(loginUserData: LoginUserData) {
        try {
            val response = withContext(Dispatchers.IO) {
                user_api.loginUser(loginUserData)
            }

            if (response.isSuccessful) {
                _loginStateFlow.value = Result.success(Unit)
                userRepository.set_auth_token(response.body()!!.token)
            } else {
                _loginStateFlow.value = Result.failure(Exception("Login failed: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            _loginStateFlow.value = Result.failure(e)
        }
    }

    suspend fun registerUser(registerData: RegisterUserData) {
        try {
            val response = withContext(Dispatchers.IO) {
                user_api.registerUser(registerData)
            }

            if (response.isSuccessful) {
                _registerStateFlow.value = Result.success(Unit)
            } else {
                _registerStateFlow.value = Result.failure(Exception("Login failed: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            _registerStateFlow.value = Result.failure(e)
        }
    }

    suspend fun addConversion(add_conversion_request: AddConversionRequest) {
        try {
            val response = withContext(Dispatchers.IO) {
                user_api.addConversion("Token ${userRepository.get_auth_token()}", add_conversion_request)
            }
            if (response.isSuccessful) {
                println("addConversion successful")
            }
            else {
                println("addConversion unsuccessful failed by : ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("addConversion unsuccessful failed by : ${e}")
        }
    }
}