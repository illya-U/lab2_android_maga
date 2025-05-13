package com.example.lab2.data.model.exchanger_model

data class CurrencyResponse(
    val success: Boolean,
    val message: String,
    val data: CurrencyData
)

data class CurrencyData(
    val result: Map<String, CurrencyInfo>
)

data class CurrencyInfo(
    val name: String,
    val symbol: String
)

data class ConversionRequest(
    val from: String,
    val to: String,
    val amount: Double
)

data class ConversionResponse(
    val success: Boolean,
    val message: String,
    val data: ConversionData
)

data class ConversionData(
    val result: Double
)

data class CurrencyBlockInfo(
    val id: String,
    val name: String,
    val symbol: String
)


class ExchangerRepository {
    private var currencyList: List<CurrencyBlockInfo> = emptyList()

    fun setCurrencyDict(data: CurrencyData) {
        currencyList = data.result.map { (id, info) ->
            CurrencyBlockInfo(
                id = id,
                name = info.name,
                symbol = info.symbol
            )
        }
    }

    fun getCurrencyList(): List<CurrencyBlockInfo> = currencyList

    private var amountFromCurrencyToCurrency: Double = 0.0

    fun setAmountFromCurrencyToCurrency(amountFromCurrencyToCurrency: Double) {
        this.amountFromCurrencyToCurrency = amountFromCurrencyToCurrency
    }

    fun getAmountFromCurrencyToCurrency(): Double = amountFromCurrencyToCurrency
}
