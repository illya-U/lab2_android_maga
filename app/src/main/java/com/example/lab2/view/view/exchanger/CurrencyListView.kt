package com.example.lab2.view.view.exchanger

import androidx.compose.runtime.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lab2.view.view_model.exchanger.CurrencyListViewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue

import com.example.lab2.ui.theme.Purple40 as CustomPurple

@Composable
fun CurrencyListView(viewModel: CurrencyListViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val currencies = viewModel.currencyList.collectAsState().value

    val fromCurrency by viewModel.from_currency.collectAsState()
    val toCurrency by viewModel.to_currency.collectAsState()
    val inputAmount by viewModel.input_amount.collectAsState()

    val isInputVisible by viewModel.isInputVisible.collectAsState()
    val convertedValue by viewModel.convertedValue.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Exchanger Label
        Text("Exchanger", style = MaterialTheme.typography.headlineMedium)

        // Input Field and Result
        if (isInputVisible) {
            var inputValue by remember { mutableStateOf(TextFieldValue("")) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TextField
                TextField(
                    value = inputAmount.toString(),
                    onValueChange = { viewModel.selectInputAmount(it) },
                    label = { Text("Enter value") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                // Exchange Icon in the middle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    Text("⇄", style = MaterialTheme.typography.headlineLarge)
                }

                // Converted Value Text
                Text(
                    "Converted Value: $convertedValue",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.exchangeValue() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exchange")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Two lists of buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Button List
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(currencies) { button ->
                    Button(
                        onClick = { viewModel.selectFromCurrency(button.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (button.id == fromCurrency) Color.Magenta else CustomPurple
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${button.name} ${button.symbol}")
                    }
                }
            }

            // Exchange Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text("⇄", style = MaterialTheme.typography.headlineLarge)
            }

            // Right Button List
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(currencies) { button ->
                    Button(
                        onClick = { viewModel.selectToCurrency(button.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (button.id == toCurrency) Color.Magenta else CustomPurple
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${button.name} ${button.symbol}")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}