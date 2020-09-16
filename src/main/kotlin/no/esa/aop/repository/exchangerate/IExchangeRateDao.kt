package no.esa.aop.repository.exchangerate

import no.esa.aop.integration.ecb.domain.EcbExchangeRateResponse
import no.esa.aop.repository.entity.ExchangeRateEntity

interface IExchangeRateDao {
	fun getPrevious(): EcbExchangeRateResponse
	fun saveRate(currencyEntityId: Int, rate: Double, exchangeRateRequestId: Int): ExchangeRateEntity
	fun getRatesFor(currency: String): Double
	fun getByExchangeRateResponseId(id: Int): List<ExchangeRateEntity>
}
