package no.esa.aop.integration.ecb.domain

import java.io.Serializable

data class EcbExchangeRateRequest(val base: String,
								  val date: String,
								  val rates: Map<String, Double>): Serializable
