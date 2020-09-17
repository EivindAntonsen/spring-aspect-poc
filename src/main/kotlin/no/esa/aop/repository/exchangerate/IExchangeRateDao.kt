package no.esa.aop.repository.exchangerate

import no.esa.aop.repository.entity.ExchangeRateEntity

interface IExchangeRateDao {
    fun saveRate(currencyEntityId: Int, rate: Double, exchangeRateRequestId: Int): ExchangeRateEntity
    fun getByExchangeRateResponseId(id: Int): List<ExchangeRateEntity>
}
