package com.example.lab2.view.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lab2.view.view_model.RegisterViewModel
import com.example.lab2.view.ui_components.DropdownMenuGender
import com.example.lab2.view.ui_components.DatePicker
import com.example.lab2.view.view_model.RegisterUiState

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val registerStateChanged by viewModel.registerStateChanged.collectAsState()

    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val birthDate by viewModel.birthDate.collectAsState()

    // Main UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { viewModel.updateUsername(it) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenuGender(
            selectedGender = gender,
            onGenderSelected = { viewModel.updateGender(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        DatePicker(
            selectedDate = birthDate,
            onDateSelected = { viewModel.updateBirthDate(it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.registerUser()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        // Handle UI states
        when (registerStateChanged) {
            is RegisterUiState.Idle -> {
                // Do nothing
            }
            is RegisterUiState.Loading -> {
                Text("Loading...", modifier = Modifier.padding(top = 16.dp))
            }
            is RegisterUiState.Success -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Register successful!", Toast.LENGTH_LONG).show()
                    navController.navigate("start") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
            is RegisterUiState.Error -> {
                val message = (registerStateChanged as RegisterUiState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
