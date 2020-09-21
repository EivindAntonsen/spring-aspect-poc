package no.esa.aop.resource.exchangerate

import io.swagger.annotations.*
import no.esa.aop.resource.model.Date
import no.esa.aop.resource.model.Error
import no.esa.aop.resource.model.ExchangeRateResponse
import no.esa.aop.utils.Outcome
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.Valid

@Api(value = "ExchangeRateApi", description = "Get latest exchange rates for selected currencies.")
@RequestMapping("\${api.base-path:}")
interface ExchangeRateApi {

	@ApiOperation(value = "Get latest exchange rates")
	@ApiResponses(ApiResponse(code = 200, message = "Success", response = Outcome::class),
				  ApiResponse(code = 400, message = "Bad request", response = Outcome::class),
				  ApiResponse(code = 500, message = "Internal server error", response = Error::class))
	@RequestMapping(value = ["/exchange-rates/latest"],
					produces = ["application/json"],
					method = [RequestMethod.GET])
	fun getLatestExchangeRates(@RequestParam("baseCurrencySymbol")
							   @ApiParam(required = false, value = "base currency for exchange rates. Defaults to EUR.")
							   baseCurrencySymbol: String?): ResponseEntity<Outcome<ExchangeRateResponse>>

	@ApiOperation(value = "Get historic exchange rates for a specific date")
	@ApiResponses(ApiResponse(code = 200, message = "Success", response = ExchangeRateResponse::class),
				  ApiResponse(code = 500, message = "Internal server error", response = Error::class))
	@RequestMapping(value = ["/exchange-rates/{date}"],
					produces = ["application/json"],
					method = [RequestMethod.GET])
	fun getRatesForDay(@PathVariable("date")
					   @Valid
					   date: Date): ResponseEntity<Outcome<ExchangeRateResponse>>

	@ApiOperation(value = "Get latest exchange rates for specific currencies")
	@ApiResponses(ApiResponse(code = 200, message = "Success", response = ExchangeRateResponse::class),
				  ApiResponse(code = 500, message = "Internal server error", response = Error::class))
	@RequestMapping(value = ["/exchange-rates"],
					produces = ["application/json"],
					method = [RequestMethod.GET])
	fun getLatestRatesFor(@RequestParam("symbols")
						  symbols: List<String>): ResponseEntity<Outcome<ExchangeRateResponse>>

	@ApiOperation(value = "Get previous exchange rates")
	@ApiResponses(ApiResponse(code = 200, message = "Success", response = ExchangeRateResponse::class),
				  ApiResponse(code = 204, message = "No previous response recorded.", response = ExchangeRateResponse::class),
				  ApiResponse(code = 500, message = "Internal server error", response = Error::class))
	@RequestMapping(value = ["/exchange-rates/previous"], produces = ["application/json"], method = [RequestMethod.GET])
	fun getPreviousExchangeRates(): ResponseEntity<ExchangeRateResponse>

	@ApiOperation(value = "test datatypes")
	@ApiResponses(ApiResponse(code = 200, message = "Success", response = ExchangeRateResponse::class))
	@RequestMapping(value = ["/test"], produces = ["application/json"], method = [RequestMethod.GET])
	fun testDataTypes()


}
