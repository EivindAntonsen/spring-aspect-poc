package no.esa.aop.service.exchangerate

import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.utils.Outcome

interface IExchangeRateService {
    fun getLatestRates(baseCurrencySymbol: String?): Outcome<ExchangeRateResponse>
    fun getPreviousRates(): ExchangeRateResponse?
}
