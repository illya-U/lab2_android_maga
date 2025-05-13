package com.example.lab2.data.model.exchanger_model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CurrencyState {
    object Loading : CurrencyState()
    object Success : CurrencyState()
    data class Error(val message: String) : CurrencyState()
}

sealed class CurrencyConversionState {
    object Nothing: CurrencyConversionState()
    object Loading : CurrencyConversionState()
    object Success : CurrencyConversionState()
    data class Error(val message: String) : CurrencyConversionState()
}

class ExchangerModel @Inject constructor() {
    private val exchanger_api: ExchangerApi = ExchangerRetrofitInstance.api
    val exchangerRepository = ExchangerRepository()

    private val _currencyState = MutableStateFlow<CurrencyState>(CurrencyState.Loading)
    val currencyState: StateFlow<CurrencyState> = _currencyState.asStateFlow()

    fun fetchCurrencyList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = exchanger_api.getCurrencyList()
                exchangerRepository.setCurrencyDict(response.data)
                _currencyState.value = CurrencyState.Success
            } catch (e: Exception) {
                _currencyState.value = CurrencyState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private val _currencyConversionState = MutableStateFlow<CurrencyConversionState>(CurrencyConversionState.Nothing)
    val currencyConversionState: StateFlow<CurrencyConversionState> = _currencyConversionState.asStateFlow()

    fun getConversionValue(
        from_currency: String,
        to_currency: String,
        amount: String,
    ) {
        _currencyConversionState.value = CurrencyConversionState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = exchanger_api.convertCurrency(
                    ConversionRequest(
                        from = from_currency,
                        to = to_currency,
                        amount = amount.toDouble(),
                    )
                )
                exchangerRepository.setAmountFromCurrencyToCurrency(response.data.result)
                _currencyConversionState.value = CurrencyConversionState.Success
            } catch (e: Exception) {
                _currencyConversionState.value = CurrencyConversionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clearConversionData() {
        _currencyConversionState.value = CurrencyConversionState.Nothing
        exchangerRepository.setAmountFromCurrencyToCurrency(0.0)
    }
}