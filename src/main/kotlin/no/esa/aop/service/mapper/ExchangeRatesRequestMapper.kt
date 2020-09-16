package no.esa.aop.service.mapper

import no.esa.aop.integration.ecb.domain.EcbExchangeRateRequest
import no.esa.aop.service.domain.Currency
import no.esa.aop.service.domain.ExchangeRate
import no.esa.aop.service.domain.ExchangeRateRequest
import no.esa.aop.utils.ecbDateTimeFormatter
import java.time.LocalDate
import java.time.LocalTime

object ExchangeRatesRequestMapper {

	fun ecbRequestToDomainRequest(request: EcbExchangeRateRequest): ExchangeRateRequest {
		val baseCurrency = Currency(request.base)
		val date = LocalDate.parse(request.date, ecbDateTimeFormatter)
		val rates = request.rates.map { (symbol, rate) ->
			ExchangeRate(Currency(symbol), rate)
		}

		return ExchangeRateRequest(baseCurrency, date.atTime(LocalTime.now()), rates)
	}
}
