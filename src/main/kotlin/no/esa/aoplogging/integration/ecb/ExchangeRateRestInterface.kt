package no.esa.aoplogging.integration.ecb

import no.esa.aoplogging.annotation.Logged
import no.esa.aoplogging.enums.APIType
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
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
	fun requestExchangeRates(): ResponseEntity<String> {
		return try {
			restTemplate.getForEntity(BASE_PATH + LATEST, String::class.java)
		} catch (error: Exception) {
			error.printStackTrace()

			// Don't try this at home
			ResponseEntity.badRequest().body(error.message)
		}
	}
}
