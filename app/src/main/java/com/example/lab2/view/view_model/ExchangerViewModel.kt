package com.example.lab2.view.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.model.exchanger_model.CurrencyState
import com.example.lab2.data.model.exchanger_model.ExchangerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangerViewModel @Inject constructor(
    private val model: ExchangerModel
) : ViewModel() {
    private val _currencyState = MutableStateFlow<CurrencyState>(CurrencyState.Loading)
    val currencyState: StateFlow<CurrencyState> = _currencyState

    init {
        observeModelState()
        model.fetchCurrencyList()
    }

    private fun observeModelState() {
        viewModelScope.launch {
            model.currencyState.collect { state ->
                when (state) {
                    is CurrencyState.Success -> {
                        _currencyState.value = CurrencyState.Success
                    }
                    is CurrencyState.Error -> {
                        _currencyState.value = CurrencyState.Error(state.message)
                    }
                    is CurrencyState.Loading -> {
                        _currencyState.value = CurrencyState.Loading
                    }
                }
            }
        }
    }
}