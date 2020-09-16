package no.esa.aop.resource

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Api(value = "TestApi", description = "Sample functions for testing.")
@RequestMapping("\${api.base-path:}")
interface TestApi {

	@ApiOperation(value = "Tests various function calls to see how the logging implemented handles it.")
	@ApiResponse(code = 200, message = "Success", response = Unit::class)
	@RequestMapping(value = ["test-logging"], produces = ["application/json"], method = [RequestMethod.GET])
	fun testLogging()
}
