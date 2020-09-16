package no.esa.aop.service.mapper

import no.esa.aop.integration.ecb.domain.EcbExchangeRateResponse
import no.esa.aop.repository.entity.ExchangeRateEntity
import no.esa.aop.repository.entity.ExchangeRateResponseEntity
import no.esa.aop.service.domain.Currency
import no.esa.aop.service.domain.ExchangeRate
import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.utils.ecbDateTimeFormatter
import java.time.LocalDate
import java.time.LocalTime

object ExchangeRateResponseMapper {

	fun ecbRequestResponseToDomainResponse(response: EcbExchangeRateResponse): ExchangeRateResponse {
		val baseCurrency = Currency(response.base)
		val date = LocalDate.parse(response.date, ecbDateTimeFormatter)
		val rates = response.rates.map { (symbol, rate) ->
			ExchangeRate(Currency(symbol), rate)
		}

		return ExchangeRateResponse(baseCurrency, date.atTime(LocalTime.now()), rates)
	}
}
