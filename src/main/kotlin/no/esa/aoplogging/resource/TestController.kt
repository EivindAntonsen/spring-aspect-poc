package no.esa.aoplogging.resource

import no.esa.aoplogging.integration.ecb.ExchangeRateRestInterface
import no.esa.aoplogging.service.ITestService
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(private val testService: ITestService,
					 private val exchangeRateRestInterface: ExchangeRateRestInterface) : TestApi {

	override fun testLogging() {
		testService.testIntAsExternalApiType()
		testService.testIntWithArgsAsLogLevelInfo(5)
		testService.testStringAsLogLevelWarn()
		testService.testStringsAsApiTypeDatabase()
		testService.testMapAsApiTypeExternalAndLogLevelWarn()
		testService.testFunctionAsArgs { println("Hi!") }
		testService.testFunctionAsReturn()
		testService.toString()

		exchangeRateRestInterface.requestExchangeRates()
	}
}
