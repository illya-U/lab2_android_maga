package com.example.lab2.view.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.model.user_model.UserModel
import com.example.lab2.data.model.user_model.LoginUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

// Sealed class to represent UI states
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userModel: UserModel
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _loginStateChanged = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginStateChanged: StateFlow<LoginUiState> = _loginStateChanged.asStateFlow()

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun loginUser() {
        _loginStateChanged.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                val loginData = LoginUserData(email.value, password.value)
                userModel.loginUser(loginData)
                userModel.loginStateFlow.filterNotNull().collect { result ->
                    when {
                        result.isSuccess -> _loginStateChanged.value = LoginUiState.Success
                        result.isFailure -> _loginStateChanged.value =
                            LoginUiState.Error(result.exceptionOrNull()?.message ?: "Error")
                    }
                }
            } catch (e: Exception) {
                _loginStateChanged.value = LoginUiState.Error(e.message ?: "Error")
            }
        }
    }
}
