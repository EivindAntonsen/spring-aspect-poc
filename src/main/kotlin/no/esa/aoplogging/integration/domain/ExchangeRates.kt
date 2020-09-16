package no.esa.aoplogging.integration.domain

import java.io.Serializable

data class ExchangeRates(private val base: String,
						 private val date: String,
						 private val rates: Map<String, Double>): Serializable
