package no.esa.aop.resource.model

import java.time.LocalDate
import java.time.LocalDateTime

data class ExchangeRateResponseDTO(val date: LocalDateTime,
								   val baseCurrency: Currency,
								   val exchangeRates: List<ExchangeRate>)
