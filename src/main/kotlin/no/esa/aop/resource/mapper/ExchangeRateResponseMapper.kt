package no.esa.aop.resource.mapper

import no.esa.aop.resource.model.ExchangeRateResponseDTO
import no.esa.aop.service.domain.Currency
import no.esa.aop.service.domain.ExchangeRate
import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.resource.model.Currency as ModelCurrency
import no.esa.aop.resource.model.ExchangeRate as ModelExchangeRate

object ExchangeRateResponseMapper {

	fun domainResponseToModelResponseDTO(exchangeRateResponse: ExchangeRateResponse): ExchangeRateResponseDTO {
		val baseCurrency = domainCurrencyToModelCurrency(exchangeRateResponse.baseCurrency)

		val modelExchangeRates = exchangeRateResponse.exchangeRates.map {
			domainExchangeRateToModelExchangeRate(it)
		}

		return ExchangeRateResponseDTO(exchangeRateResponse.dateTime, baseCurrency, modelExchangeRates)
	}

	private fun domainCurrencyToModelCurrency(currency: Currency): ModelCurrency {
		return ModelCurrency(currency.symbol)
	}

	private fun domainExchangeRateToModelExchangeRate(exchangeRate: ExchangeRate): ModelExchangeRate {
		val currency = domainCurrencyToModelCurrency(exchangeRate.currency)

		return ModelExchangeRate(currency, exchangeRate.rate)
	}
}
