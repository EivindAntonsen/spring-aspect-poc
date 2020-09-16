package no.esa.aoplogging.repository.rates

import no.esa.aoplogging.integration.domain.ExchangeRates

interface IRatesDao {
	fun getPrevious(): ExchangeRates
	fun saveRates(exchangeRates: ExchangeRates): Int
	fun getRatesFor(currency: String): Double
}
