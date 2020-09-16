package no.esa.aop.resource

import no.esa.aop.integration.ecb.ExchangeRateRestInterface
import no.esa.aop.service.ITestService
import no.esa.aop.service.exchangerate.IExchangeRateService
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(private val testService: ITestService,
					 private val exchangeRateService: IExchangeRateService) : TestApi {

	override fun testLogging() {
		testService.testIntAsExternalApiType()
		testService.testIntWithArgsAsLogLevelInfo(5)
		testService.testStringAsLogLevelWarn()
		testService.testStringsAsApiTypeDatabase()
		testService.testMapAsApiTypeExternalAndLogLevelWarn()
		testService.testFunctionAsArgs { println("Hi!") }
		testService.testFunctionAsReturn()
		testService.toString()
	}
}
