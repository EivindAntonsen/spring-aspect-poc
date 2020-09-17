package no.esa.aop.resource.model

import java.time.LocalDateTime

data class ExchangeRateResponse(val date: LocalDateTime,
								val baseCurrency: String,
								val exchangeRates: List<ExchangeRate>)
