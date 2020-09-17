package no.esa.aop.service.domain

import java.time.LocalDateTime

data class ExchangeRateResponse(val baseCurrency: Currency,
								val dateTime: LocalDateTime,
								val exchangeRates: List<ExchangeRate>)
