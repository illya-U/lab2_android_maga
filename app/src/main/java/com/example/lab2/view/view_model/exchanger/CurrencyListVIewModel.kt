package com.example.lab2.view.view_model.exchanger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.model.exchanger_model.CurrencyBlockInfo
import com.example.lab2.data.model.exchanger_model.CurrencyConversionState.*
import com.example.lab2.data.model.exchanger_model.ExchangerModel
import com.example.lab2.data.model.user_model.AddConversionRequest
import com.example.lab2.data.model.user_model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.toString

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val exchanger_model: ExchangerModel,
    private val user_model: UserModel,
) : ViewModel() {
    private val _currencyList = MutableStateFlow<List<CurrencyBlockInfo>>(emptyList())
    val currencyList: StateFlow<List<CurrencyBlockInfo>> = _currencyList


    private val _from_currency = MutableStateFlow<String?>(null)
    val from_currency: StateFlow<String?> = _from_currency

    private val _to_currency = MutableStateFlow<String?>(null)
    val to_currency: StateFlow<String?> = _to_currency

    private val _input_amount = MutableStateFlow<String>("")
    val input_amount: StateFlow<String?> = _input_amount

    private val _isInputVisible = MutableStateFlow(false)
    val isInputVisible: StateFlow<Boolean> = _isInputVisible


    private val _convertedValue = MutableStateFlow("")
    val convertedValue: StateFlow<String> = _convertedValue


    init {
        loadCurrencies()
        observeConvertedValueChanged()
    }

    override fun onCleared() {
        exchanger_model.clearConversionData()
        super.onCleared()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            _currencyList.value = exchanger_model.exchangerRepository.getCurrencyList()
        }
    }

    fun selectFromCurrency(from_currency: String) {
        _from_currency.value = from_currency
        checkInputVisibility()
    }

    fun selectToCurrency(to_currency: String) {
        _to_currency.value = to_currency
        checkInputVisibility()
    }

    fun selectInputAmount(input_ammount: String) {
        _input_amount.value = input_ammount
    }

    private fun checkInputVisibility() {
        _isInputVisible.value = _from_currency.value != null && _to_currency.value != null
    }

    fun exchangeValue() {
        viewModelScope.launch {
            exchanger_model.getConversionValue(
                from_currency = _from_currency.value.toString(),
                to_currency = _to_currency.value.toString(),
                amount = _input_amount.value
            )
        }
    }

    fun observeConvertedValueChanged() {
        viewModelScope.launch {
            exchanger_model.currencyConversionState.collect { state ->
                when (state) {
                    is Success -> {
                        _convertedValue.value = exchanger_model.exchangerRepository.getAmountFromCurrencyToCurrency().toString()
                        user_model.addConversion(
                            AddConversionRequest (
                                amount = String.format("%.3f", exchanger_model.exchangerRepository.getAmountFromCurrencyToCurrency()).toDouble(),
                                from_currency = _from_currency.value.toString(),
                                to_currency = _to_currency.value.toString(),
                            )
                        )
                    }
                    is Error -> {
                        _convertedValue.value = "Sorry, Something bad is happened"
                    }
                    is Loading -> {
                        _convertedValue.value = "Pls wait. Work in progress"
                    }
                    Nothing -> {
                        _convertedValue.value = "\uD83E\uDD11"
                    }
                }
            }
        }
    }
}