package no.esa.aop.service.exchangerate

import no.esa.aop.service.domain.ExchangeRateResponse

interface IExchangeRateService {
    fun getLatestRates(baseCurrencySymbol: String?): ExchangeRateResponse
    fun getPreviousRates(): ExchangeRateResponse
}
