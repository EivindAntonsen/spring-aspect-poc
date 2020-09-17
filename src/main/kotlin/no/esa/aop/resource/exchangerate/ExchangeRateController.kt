package no.esa.aop.resource.exchangerate

import no.esa.aop.resource.exception.ApiException
import no.esa.aop.resource.mapper.ExchangeRateResponseMapper
import no.esa.aop.resource.model.ExchangeRateResponseDTO
import no.esa.aop.service.exception.BadExchangeRateRequestException
import no.esa.aop.service.exception.CouldNotGetEcbExchangeRateResponseException
import no.esa.aop.service.exception.NoPreviousExchangeRateResponseException
import no.esa.aop.service.exchangerate.IExchangeRateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ExchangeRateController(private val exchangeRateService: IExchangeRateService) : ExchangeRateApi {

    override fun getLatestExchangeRates(baseCurrencySymbol: String?): ResponseEntity<ExchangeRateResponseDTO> {
        val result = try {
            exchangeRateService.getLatestRates(baseCurrencySymbol?.toUpperCase()?.trim())
        } catch (exception: BadExchangeRateRequestException) {
            throw ApiException.BadRequestException(exception.message)
        } catch (exception: CouldNotGetEcbExchangeRateResponseException) {
            throw ApiException.UnknownErrorException(exception.message)
        }

        val dto = ExchangeRateResponseMapper.domainResponseToModelResponseDTO(result)

        return ResponseEntity.ok(dto)
    }

    override fun getPreviousExchangeRates(): ResponseEntity<ExchangeRateResponseDTO> {
        val result = try {
            exchangeRateService.getPreviousRates()
        } catch (exception: NoPreviousExchangeRateResponseException) {
            throw ApiException.NoPreviouslyRecordedResponseException(exception.message)
        }

        val dto = ExchangeRateResponseMapper.domainResponseToModelResponseDTO(result)

        return ResponseEntity.ok(dto)
    }
}
