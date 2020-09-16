package no.esa.aop.repository.exchangerate

import no.esa.aop.integration.ecb.domain.EcbExchangeRateRequest
import no.esa.aop.repository.entity.ExchangeRateEntity

interface IExchangeRateDao {
	fun getPrevious(): EcbExchangeRateRequest
	fun saveRate(currencyEntityId: Int, rate: Double, exchangeRateRequestId: Int): ExchangeRateEntity
	fun getRatesFor(currency: String): Double
}
