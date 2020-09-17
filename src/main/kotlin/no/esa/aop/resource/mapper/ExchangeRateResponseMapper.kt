package no.esa.aop.resource.mapper

import no.esa.aop.resource.model.ExchangeRateResponseDTO
import no.esa.aop.service.domain.Currency
import no.esa.aop.service.domain.ExchangeRate
import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.resource.model.Currency as ModelCurrency
import no.esa.aop.resource.model.ExchangeRate as ModelExchangeRate

object ExchangeRateResponseMapper {

    fun domainResponseToModelResponseDTO(exchangeRateResponse: ExchangeRateResponse): ExchangeRateResponseDTO {
        val baseCurrency = exchangeRateResponse.baseCurrency

        val modelExchangeRates = exchangeRateResponse.exchangeRates.map {
            domainExchangeRateToModelExchangeRate(it)
        }

        return ExchangeRateResponseDTO(exchangeRateResponse.dateTime, baseCurrency.symbol, modelExchangeRates)
    }

    private fun domainExchangeRateToModelExchangeRate(exchangeRate: ExchangeRate): ModelExchangeRate {
        return ModelExchangeRate(exchangeRate.currency.symbol, exchangeRate.rate)
    }
}
