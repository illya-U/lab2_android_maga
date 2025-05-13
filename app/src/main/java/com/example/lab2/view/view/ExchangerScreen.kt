package com.example.lab2.view.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lab2.data.model.exchanger_model.CurrencyState
import com.example.lab2.view.view_model.ExchangerViewModel
import com.example.lab2.R
import com.example.lab2.view.view.exchanger.CurrencyListView


@Composable
fun ExchangerScreen(viewModel: ExchangerViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val status = viewModel.currencyState.collectAsState().value

    when (status) {
        is CurrencyState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.cat_placeholder),
                    contentDescription = "Loading Cat",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Please wait while we load the currencies...")
            }
        }

        is CurrencyState.Error -> {
            // Toast for popup message
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Server error: ${status.message}", Toast.LENGTH_LONG).show()
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sorry, internal error occurred.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is CurrencyState.Success -> {
            CurrencyListView()
        }
    }
}