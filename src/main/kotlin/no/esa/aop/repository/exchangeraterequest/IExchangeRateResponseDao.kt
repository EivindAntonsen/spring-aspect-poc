package no.esa.aop.repository.exchangeraterequest

import no.esa.aop.repository.entity.ExchangeRateResponseEntity
import java.time.LocalDateTime

interface IExchangeRateResponseDao {
	fun save(dateTime: LocalDateTime, baseCurrencyEntityId: Int): ExchangeRateResponseEntity
	fun get(id: Int): ExchangeRateResponseEntity?
	fun getAll(): List<ExchangeRateResponseEntity>
}
