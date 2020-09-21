package no.esa.aop.service.exchangerate

import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.utils.Outcome
import java.time.LocalDate

interface IExchangeRateService {
	fun getLatestRates(baseCurrencySymbol: String?): Outcome<ExchangeRateResponse>
	fun getPreviousRates(): ExchangeRateResponse?
	fun getRatesForDay(localDate: LocalDate): Outcome<ExchangeRateResponse>
	fun getLatesRatesFor(symbols: List<String>): Outcome<ExchangeRateResponse>
}
