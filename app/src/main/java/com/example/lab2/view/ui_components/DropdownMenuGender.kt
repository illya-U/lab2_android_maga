package com.example.lab2.view.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.lab2.view.view_model.Gender

@Composable
fun DropdownMenuGender(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    val genders = listOf(Gender.M, Gender.F, Gender.O)
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedGender.toUIRepresentation())
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = {Text(gender.toUIRepresentation())},
                    onClick = {
                        expanded = false
                        onGenderSelected(gender)
                    }
                )
            }
        }
    }
}
