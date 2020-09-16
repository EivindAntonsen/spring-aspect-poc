package no.esa.aoplogging.integration.ecb

import no.esa.aoplogging.annotation.Logged
import no.esa.aoplogging.enums.APIType
import no.esa.aoplogging.integration.domain.ExchangeRates
import org.springframework.boot.logging.LogLevel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ExchangeRateRestInterface(private val restTemplate: RestTemplate) {

	companion object {
		private const val BASE_PATH = "https://api.exchangeratesapi.io/"
		private const val LATEST = "latest"
	}

	@Logged(apiType = APIType.EXTERNAL, logLevel = LogLevel.INFO)
	fun requestExchangeRates(): ResponseEntity<ExchangeRates> {
		return restTemplate.getForEntity(BASE_PATH + LATEST, ExchangeRates::class.java)
	}
}
