package no.esa.aop.integration.ecb

import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.integration.ecb.domain.EcbExchangeRateRequest
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
	fun requestExchangeRates(): ResponseEntity<EcbExchangeRateRequest> {
		return restTemplate.getForEntity(BASE_PATH + LATEST, EcbExchangeRateRequest::class.java)
	}
}
