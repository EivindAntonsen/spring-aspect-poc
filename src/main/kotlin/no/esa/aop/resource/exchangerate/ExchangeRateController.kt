package no.esa.aop.resource.exchangerate

import no.esa.aop.resource.mapper.ExchangeRateResponseMapper
import no.esa.aop.resource.model.ExchangeRateResponseDTO
import no.esa.aop.service.exchangerate.IExchangeRateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ExchangeRateController(private val exchangeRateService: IExchangeRateService): ExchangeRateApi {

	override fun getLatestExchangeRates(): ResponseEntity<ExchangeRateResponseDTO> {
		val response = exchangeRateService.getLatestRates()

		val dto = ExchangeRateResponseMapper.domainResponseToModelResponseDTO(response)

		return ResponseEntity.ok(dto)
	}

	override fun getPreviousExchangeRates(): ResponseEntity<ExchangeRateResponseDTO> {
		val response = exchangeRateService.getPreviousRates()

		val dto = ExchangeRateResponseMapper.domainResponseToModelResponseDTO(response)

		return ResponseEntity.ok(dto)
	}
}
