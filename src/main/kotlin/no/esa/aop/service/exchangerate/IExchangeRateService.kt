package no.esa.aop.service.exchangerate

import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.utils.Outcome
import java.time.LocalDate

interface IExchangeRateService {
	fun getLatestRates(baseCurrencySymbol: String?): Outcome<ExchangeRateResponse>
	fun getPreviousRates(): ExchangeRateResponse?
	fun getRatesForDay(localDate: LocalDate): Outcome<ExchangeRateResponse>
	fun getLatesRatesFor(symbols: List<String>): Outcome<ExchangeRateResponse>

	fun anonymousObject(id: Int): Any
	fun function(): () -> Unit
	fun <T> generic(t: T): () -> T
	fun denseStructure(): Map<Pair<String, Double>, Map<List<Int>, String>>
	fun map(): Map<String, String>
	fun int(): Int
	fun string(): String
}
