package com.example.lab2.view.ui_components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }

    // Button to open the DatePicker dialog
    TextButton(onClick = { showDatePickerDialog = true }) {
        Text(
            text = selectedDate?.toString() ?: "Select Date",
            modifier = Modifier.padding(8.dp)
        )
    }

    if (showDatePickerDialog) {
        // Material 3 DatePicker Dialog
        val currentYear = LocalDate.now().year // Get the current year
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.toEpochMilli(),
            yearRange = 1900..currentYear // Dynamically set range from 1900 to the current year
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        onDateSelected(selectedMillis.toLocalDate())
                    }
                    showDatePickerDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Extension function to convert LocalDate to epoch milliseconds
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toEpochMilli(): Long {
    return this.toEpochDay() * 24 * 60 * 60 * 1000 // Convert days to milliseconds
}

@RequiresApi(Build.VERSION_CODES.O)
// Extension function to convert epoch milliseconds to LocalDate
fun Long.toLocalDate(): LocalDate {
    return LocalDate.ofEpochDay(this / (24 * 60 * 60 * 1000)) // Convert milliseconds to days
}
