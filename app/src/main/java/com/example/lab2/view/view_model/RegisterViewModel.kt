package com.example.lab2.view.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.model.user_model.GenderToBackendNameMapper
import com.example.lab2.data.model.user_model.RegisterUserData
import com.example.lab2.data.model.user_model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class Gender {
    object M : Gender()
    object F : Gender()
    object O : Gender()
    object Undefined: Gender()

    fun toUIRepresentation(): String {
        return when (this) {
            M -> "Male"
            F -> "Female"
            O -> "Other"
            Undefined -> "None"
        }
    }
}



// Sealed class to represent UI states
sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userModel: UserModel
) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _gender = MutableStateFlow<Gender>(Gender.M)
    val gender: StateFlow<Gender> = _gender.asStateFlow()

    private val _birthDate = MutableStateFlow<LocalDate?>(null)
    val birthDate: StateFlow<LocalDate?> = _birthDate.asStateFlow()

    private val _registerStateChanged = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerStateChanged: StateFlow<RegisterUiState> = _registerStateChanged.asStateFlow()

    fun updateUsername(value: String) {
        _username.value = value
    }

    fun updateEmail(value: String) {
        _email.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun updateGender(value: Gender) {
        _gender.value = value
    }

    fun updateBirthDate(value: LocalDate) {
        _birthDate.value = value
    }

    fun registerUser() {
        _registerStateChanged.value = RegisterUiState.Loading

        viewModelScope.launch {
            try {
                val registerData = RegisterUserData(
                    username = username.value,
                    email = email.value,
                    password = password.value,
                    gender = GenderToBackendNameMapper.toString(gender.value),
                    birth_date = birthDate.value.toString(),
                )
                userModel.registerUser(registerData)
                userModel.registerStateFlow.filterNotNull().collect { result ->
                    when {
                        result.isSuccess -> _registerStateChanged.value = RegisterUiState.Success
                        result.isFailure -> _registerStateChanged.value =
                            RegisterUiState.Error(result.exceptionOrNull()?.message ?: "Error")
                    }
                }
            } catch (e: Exception) {
                _registerStateChanged.value = RegisterUiState.Error(e.message ?: "Error")
            }
        }
    }
}
