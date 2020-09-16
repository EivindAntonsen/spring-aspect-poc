package no.esa.aop.repository.entity

import java.time.LocalDateTime

data class ExchangeRateResponseEntity(val id: Int,
									  val baseCurrencyId: Int,
									  val dateTime: LocalDateTime)
