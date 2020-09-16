package no.esa.aop.repository.exchangeraterequest

import no.esa.aop.repository.entity.ExchangeRateRequestEntity
import java.time.LocalDateTime

interface IExchangeRateRequestDao {
	fun save(dateTime: LocalDateTime, baseCurrencyEntityId: Int): ExchangeRateRequestEntity
	fun get(id: Int): ExchangeRateRequestEntity
}
