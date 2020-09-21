package no.esa.aop.service.mapper

import no.esa.aop.integration.ecb.domain.EcbExchangeRateResponse
import no.esa.aop.service.domain.Currency
import no.esa.aop.service.domain.ExchangeRate
import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.utils.Outcome
import no.esa.aop.utils.ecbDateTimeFormatter
import java.time.LocalDate
import java.time.LocalTime

object EcbExchangeRateResponseMapper {
	fun ecbRequestResponseToDomainResponse(response: Outcome<EcbExchangeRateResponse>): Outcome<ExchangeRateResponse> {
		return when (response) {
			is Outcome.Success -> {
				val baseCurrency = Currency(response.value.base)
				val date = LocalDate.parse(response.value.date, ecbDateTimeFormatter)
				val rates = response.value.rates.map { (symbol, rate) ->
					ExchangeRate(Currency(symbol), rate)
				}

				Outcome.Success(ExchangeRateResponse(baseCurrency, date.atTime(LocalTime.now()), rates))
			}
			is Outcome.Error -> Outcome.Error(response.message, response.cause)
		}
	}
}
