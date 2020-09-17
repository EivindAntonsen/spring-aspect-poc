package no.esa.aop.integration.ecb

import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.integration.ecb.domain.EcbExchangeRateResponse
import no.esa.aop.service.exception.BadExchangeRateRequestException
import no.esa.aop.service.exception.CouldNotGetEcbExchangeRateResponseException
import org.springframework.boot.logging.LogLevel
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class ExchangeRateRestInterface(private val restTemplate: RestTemplate) {

    companion object {
        private const val BASE_URL = "https://api.exchangeratesapi.io/"
        private const val ENDPOINT = "latest"
        private const val DEFAULT_CURRENCY_BASE = "EUR"
    }

    @Logged(apiType = APIType.EXTERNAL, logLevel = LogLevel.INFO)
    fun requestExchangeRates(baseCurrencySymbol: String?): EcbExchangeRateResponse {
        val url = buildURL(BASE_URL + ENDPOINT, "base" to (baseCurrencySymbol ?: DEFAULT_CURRENCY_BASE))

        val responseEntity = try {
            restTemplate.getForEntity(url, EcbExchangeRateResponse::class.java)
        } catch (exception: HttpClientErrorException) {
            throw BadExchangeRateRequestException(exception.message)
        } catch (exception: Exception) {
            throw CouldNotGetEcbExchangeRateResponseException(exception.message)
        }

        return if (!responseEntity.statusCode.is2xxSuccessful) {
            throw RuntimeException("Response status code was not 2xx ok!")
        } else responseEntity.body!!
    }

    fun buildURL(url: String, queryParameter: Pair<String, Any>): String {
        return UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam(queryParameter.first, queryParameter.second)
                .build()
                .toUriString()
    }
}
