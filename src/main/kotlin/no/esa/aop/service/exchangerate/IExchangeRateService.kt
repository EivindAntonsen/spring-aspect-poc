package no.esa.aop.service.exchangerate

import no.esa.aop.service.domain.ExchangeRateRequest

interface IExchangeRateService {
	fun getLatestRates(): ExchangeRateRequest
}
