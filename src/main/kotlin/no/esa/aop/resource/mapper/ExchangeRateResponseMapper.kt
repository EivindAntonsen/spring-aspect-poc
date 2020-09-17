package no.esa.aop.resource.mapper

import no.esa.aop.service.domain.ExchangeRate
import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.utils.Outcome
import no.esa.aop.resource.model.ExchangeRate as ModelExchangeRate
import no.esa.aop.resource.model.ExchangeRateResponse as ModelExchangeRateResponse

object ExchangeRateResponseMapper {

	fun domainResponseToModelResponseDTO(response: Outcome<ExchangeRateResponse>): Outcome<ModelExchangeRateResponse> {
		return when (response) {
			is Outcome.Success -> {
				val baseCurrency = response.value.baseCurrency

				val modelExchangeRates = response.value.exchangeRates.map {
					domainExchangeRateToModelExchangeRate(it)
				}

				Outcome.Success(ModelExchangeRateResponse(response.value.dateTime,
														  baseCurrency.symbol,
														  modelExchangeRates))
			}
			is Outcome.Error -> response
		}
	}

	fun exchangeRateResponseToModelResponseDTO(response: ExchangeRateResponse): ModelExchangeRateResponse {
		val baseCurrency = response.baseCurrency

		val modelExchangeRates = response.exchangeRates.map {
			domainExchangeRateToModelExchangeRate(it)
		}

		return ModelExchangeRateResponse(response.dateTime,
										 baseCurrency.symbol,
										 modelExchangeRates)
	}

	private fun domainExchangeRateToModelExchangeRate(exchangeRate: ExchangeRate): ModelExchangeRate {
		return ModelExchangeRate(exchangeRate.currency.symbol, exchangeRate.rate)
	}
}
