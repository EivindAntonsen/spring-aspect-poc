package no.esa.aop.integration.ecb

import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.integration.ecb.domain.EcbExchangeRateResponse
import no.esa.aop.utils.Outcome
import no.esa.aop.utils.ecbDateTimeFormatter
import org.springframework.boot.logging.LogLevel
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate

@Component
class ExchangeRateRestInterface(private val restTemplate: RestTemplate) {

	companion object {
		private const val BASE_URL = "https://api.exchangeratesapi.io/"
		private const val ENDPOINT_LATEST = "latest"
		private const val DEFAULT_CURRENCY_BASE = "EUR"
	}

	@Logged(apiType = APIType.EXTERNAL, logLevel = LogLevel.INFO)
	fun getRatesForDay(localDate: LocalDate): Outcome<EcbExchangeRateResponse> {
		val url = buildURL(BASE_URL + localDate.format(ecbDateTimeFormatter), emptyMap())

		return dispatchRequest(url)
	}

	@Logged(apiType = APIType.EXTERNAL, logLevel = LogLevel.INFO)
	fun getLatesRatesFor(symbols: List<String>): Outcome<EcbExchangeRateResponse> {
		val queryParameters = mapOf("symbols" to symbols.joinToString(",") { it.toUpperCase() })
		val url = buildURL(BASE_URL + ENDPOINT_LATEST, queryParameters)

		return dispatchRequest(url)
	}

	@Logged(apiType = APIType.EXTERNAL, logLevel = LogLevel.INFO)
	fun requestExchangeRates(baseCurrencySymbol: String?): Outcome<EcbExchangeRateResponse> {
		val queryParameters = mapOf("base" to (baseCurrencySymbol ?: DEFAULT_CURRENCY_BASE).toUpperCase())
		val url = buildURL(BASE_URL + ENDPOINT_LATEST, queryParameters)

		return dispatchRequest(url)
	}

	private fun dispatchRequest(url: String): Outcome<EcbExchangeRateResponse> {
		return try {
			val ecbExchangeRateResponse = restTemplate.getForEntity(url, EcbExchangeRateResponse::class.java)

			if (ecbExchangeRateResponse.statusCode.is2xxSuccessful) {
				Outcome.Success(ecbExchangeRateResponse.body!!)
			} else Outcome.Error("Response did not contain exchange rates.", null)
		} catch (exception: Exception) {
			val errorMessage = exception.message ?: when (exception) {
				is HttpServerErrorException -> "Exchange rate request failed due to an external server error."
				is HttpClientErrorException -> "Exchange rate request failed, most likely due to a bad request."
				else -> "Exchange rate request failed for an unknown cause."
			}

			Outcome.Error(errorMessage, exception)
		}
	}

	fun buildURL(url: String, queryParameters: Map<String, String>): String {
		return UriComponentsBuilder
				.fromHttpUrl(url).apply {
					queryParameters.forEach { (param, value) ->
						queryParam(param, value)
					}
				}.build()
				.toUriString()
	}
}
