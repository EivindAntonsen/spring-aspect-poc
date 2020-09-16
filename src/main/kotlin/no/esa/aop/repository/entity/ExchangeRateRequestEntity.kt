package no.esa.aop.repository.entity

import java.time.LocalDateTime

data class ExchangeRateRequestEntity(val id: Int,
									 val baseCurrencyId: Int,
									 val dateTime: LocalDateTime)
