package no.esa.aop.resource.exchangerate

import no.esa.aop.resource.mapper.ExchangeRateResponseMapper
import no.esa.aop.resource.model.ExchangeRateResponse
import no.esa.aop.service.exchangerate.IExchangeRateService
import no.esa.aop.utils.Outcome
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ExchangeRateController(private val exchangeRateService: IExchangeRateService) : ExchangeRateApi {

    override fun getLatestExchangeRates(baseCurrencySymbol: String?): ResponseEntity<Outcome<ExchangeRateResponse>> {
        val result = exchangeRateService.getLatestRates(baseCurrencySymbol).let {
            ExchangeRateResponseMapper.domainResponseToModelResponseDTO(it)
        }

        return when (result) {
			is Outcome.Success -> ResponseEntity.ok(result)
			is Outcome.Error -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Outcome.Error(result.message))
        }
    }

    override fun getPreviousExchangeRates(): ResponseEntity<ExchangeRateResponse> {
        return exchangeRateService.getPreviousRates().let { result ->
            if (result != null) {
                val dto = ExchangeRateResponseMapper.exchangeRateResponseToModelResponseDTO(result)

                ResponseEntity.ok(dto)
            } else ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        }
    }
}
