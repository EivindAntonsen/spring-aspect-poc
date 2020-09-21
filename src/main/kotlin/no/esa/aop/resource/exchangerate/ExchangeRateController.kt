package no.esa.aop.resource.exchangerate

import no.esa.aop.annotation.Logged
import no.esa.aop.resource.mapper.ExchangeRateResponseMapper
import no.esa.aop.resource.model.Date
import no.esa.aop.resource.model.ExchangeRateResponse
import no.esa.aop.service.exchangerate.IExchangeRateService
import no.esa.aop.utils.Outcome
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class ExchangeRateController(private val exchangeRateService: IExchangeRateService) : ExchangeRateApi {

    @CrossOrigin(origins = ["http://localhost:3000"])
    override fun getRatesForDay(date: Date): ResponseEntity<Outcome<ExchangeRateResponse>> {
        val result = exchangeRateService.getRatesForDay(date.value).let {
            ExchangeRateResponseMapper.domainResponseToModelResponseDTO(it)
        }

        return when (result) {
            is Outcome.Success -> ResponseEntity.ok(result)
            is Outcome.Error -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Outcome.Error(result.message))
        }
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    override fun getLatestRatesFor(symbols: List<String>): ResponseEntity<Outcome<ExchangeRateResponse>> {
        val result = exchangeRateService.getLatesRatesFor(symbols).let {
            ExchangeRateResponseMapper.domainResponseToModelResponseDTO(it)
        }

        return when (result) {
            is Outcome.Success -> ResponseEntity.ok(result)
            is Outcome.Error -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Outcome.Error(result.message))
        }
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    override fun getLatestExchangeRates(baseCurrencySymbol: String?): ResponseEntity<Outcome<ExchangeRateResponse>> {
        val result = exchangeRateService.getLatestRates(baseCurrencySymbol).let {
            ExchangeRateResponseMapper.domainResponseToModelResponseDTO(it)
        }

        return when (result) {
            is Outcome.Success -> ResponseEntity.ok(result)
            is Outcome.Error -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Outcome.Error(result.message))
        }
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    override fun getPreviousExchangeRates(): ResponseEntity<ExchangeRateResponse> {
        return exchangeRateService.getPreviousRates().let { result ->
            if (result != null) {
                val dto = ExchangeRateResponseMapper.exchangeRateResponseToModelResponseDTO(result)

                ResponseEntity.ok(dto)
            } else ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        }
    }

    override fun testDataTypes() {
        with(exchangeRateService) {
            string()
            int()
            map()
            denseStructure()
            generic(BigDecimal(250))
            function()
            anonymousObject(23)
        }
    }
}
