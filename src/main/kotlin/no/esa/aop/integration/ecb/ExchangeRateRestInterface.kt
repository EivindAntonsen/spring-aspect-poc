package no.esa.aop.integration.ecb

import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.integration.ecb.domain.EcbExchangeRateResponse
import org.springframework.boot.logging.LogLevel
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ExchangeRateRestInterface(private val restTemplate: RestTemplate) {

	companion object {
		private const val BASE_URL = "https://api.exchangeratesapi.io/"
		private const val ENDPOINT = "latest"
	}

	@Logged(apiType = APIType.EXTERNAL, logLevel = LogLevel.INFO)
	fun requestExchangeRates(): EcbExchangeRateResponse {
		val responseEntity = restTemplate.getForEntity(BASE_URL + ENDPOINT, EcbExchangeRateResponse::class.java)

		return if (!responseEntity.statusCode.is2xxSuccessful) {
			throw RuntimeException("Response status code was not 2xx ok!")
		} else responseEntity.body!!
	}
}
