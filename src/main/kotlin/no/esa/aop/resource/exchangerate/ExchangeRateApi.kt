package no.esa.aop.resource.exchangerate

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import no.esa.aop.resource.model.ExchangeRateResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Api(value = "ExchangeRateApi", description = "Get latest exchange rates for selected currencies.")
@RequestMapping("\${api.base-path:}")
interface ExchangeRateApi {

	@ApiOperation(value = "Get latest exchange rates")
	@ApiResponses(ApiResponse(code = 200, message = "Success", response = ExchangeRateResponseDTO::class))
	@RequestMapping(value = ["/exchange-rates/latest"], produces = ["application/json"], method = [RequestMethod.GET])
	fun getLatestExchangeRates(): ResponseEntity<ExchangeRateResponseDTO>

	@ApiOperation(value = "Get previous exchange rates")
	@ApiResponses(ApiResponse(code = 200, message = "Success", response = ExchangeRateResponseDTO::class))
	@RequestMapping(value = ["/exchange-rates/previous"], produces = ["application/json"], method = [RequestMethod.GET])
	fun getPreviousExchangeRates(): ResponseEntity<ExchangeRateResponseDTO>
}
