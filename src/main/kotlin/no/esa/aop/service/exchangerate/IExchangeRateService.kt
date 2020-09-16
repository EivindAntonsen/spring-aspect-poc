package no.esa.aop.service.exchangerate

import no.esa.aop.repository.entity.ExchangeRateResponseEntity
import no.esa.aop.service.domain.ExchangeRateResponse

interface IExchangeRateService {
	fun getLatestRates(): ExchangeRateResponse
	fun getPreviousRates(): ExchangeRateResponse
}
