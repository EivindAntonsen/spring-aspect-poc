package no.esa.aop.resource.model

import java.time.LocalDateTime

data class ExchangeRateResponseDTO(val date: LocalDateTime,
                                   val baseCurrency: String,
                                   val exchangeRates: List<ExchangeRate>)
