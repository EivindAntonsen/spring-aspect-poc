package no.esa.aop.service.exchangerate

import no.esa.aop.integration.ecb.ExchangeRateRestInterface
import no.esa.aop.repository.currency.ICurrencyDao
import no.esa.aop.repository.entity.CurrencyEntity
import no.esa.aop.repository.entity.ExchangeRateEntity
import no.esa.aop.repository.entity.ExchangeRateResponseEntity
import no.esa.aop.repository.exchangerate.IExchangeRateDao
import no.esa.aop.repository.exchangeraterequest.IExchangeRateResponseDao
import no.esa.aop.service.domain.Currency
import no.esa.aop.service.domain.ExchangeRate
import no.esa.aop.service.domain.ExchangeRateResponse
import no.esa.aop.service.exception.NoPreviousExchangeRateResponseException
import no.esa.aop.service.mapper.ExchangeRateResponseMapper
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(private val exchangeRateRestInterface: ExchangeRateRestInterface,
						  private val currencyDao: ICurrencyDao,
						  private val exchangeRateDao: IExchangeRateDao,
						  private val exchangeRateResponseDao: IExchangeRateResponseDao) : IExchangeRateService {

    override fun getLatestRates(baseCurrencySymbol: String?): ExchangeRateResponse {
        val ecbExchangeRatesResponse = exchangeRateRestInterface.requestExchangeRates(baseCurrencySymbol)

        val exchangeRateResponse = ExchangeRateResponseMapper.ecbRequestResponseToDomainResponse(ecbExchangeRatesResponse)

        saveResponse(exchangeRateResponse)

        return exchangeRateResponse
    }

    override fun getPreviousRates(): ExchangeRateResponse {
        val exchangeRateResponseEntity = exchangeRateResponseDao.getAll().maxByOrNull {
            it.id
        } ?: throw NoPreviousExchangeRateResponseException()

        val currencyEntities = currencyDao.getAll()

        val baseCurrency = currencyEntities.first {
            it.id == exchangeRateResponseEntity.baseCurrencyId
        }.let { Currency(it.symbol) }

        val exchangeRates = exchangeRateDao.getByExchangeRateResponseId(exchangeRateResponseEntity.id).map { exchangeRateEntity ->
            val symbol = currencyEntities.first { currencyEntity ->
                currencyEntity.id == exchangeRateEntity.currencyId
            }.symbol

            ExchangeRate(Currency(symbol), exchangeRateEntity.rate)
        }

        return ExchangeRateResponse(baseCurrency, exchangeRateResponseEntity.dateTime, exchangeRates)
    }

    private fun getCurrencyEntityIdsAndExchangeRates(currencyEntities: List<CurrencyEntity>,
													 baseCurrencyEntityId: Int,
													 exchangeRates: List<ExchangeRate>): Map<Int, Double> {

        return currencyEntities.filterNot { currencyEntity ->
            currencyEntity.id == baseCurrencyEntityId
        }.map { currencyEntity ->
            currencyEntity.id to exchangeRates.first { rate ->
                rate.currency.symbol == currencyEntity.symbol
            }.rate
        }.toMap()
    }

    private fun getBaseCurrencyEntityId(currencyEntities: List<CurrencyEntity>, baseCurrencySymbol: String): CurrencyEntity {
        return currencyEntities.first { currencyEntity ->
            currencyEntity.symbol.toLowerCase() == baseCurrencySymbol.toLowerCase()
        }
    }

    private fun saveResponse(exchangeRateResponse: ExchangeRateResponse): Pair<ExchangeRateResponseEntity, List<ExchangeRateEntity>> {
        saveNewCurrencies(exchangeRateResponse)
        val currencyEntities = currencyDao.getAll()

        val baseCurrencyEntity = getBaseCurrencyEntityId(currencyEntities, exchangeRateResponse.baseCurrency.symbol)
        val exchangeRatesResponseEntity = exchangeRateResponseDao.save(exchangeRateResponse.dateTime,
																	   baseCurrencyEntity.id)
        val currencyEntityIdsAndExchangeRates = getCurrencyEntityIdsAndExchangeRates(currencyEntities,
																					 baseCurrencyEntity.id,
																					 exchangeRateResponse.exchangeRates)
        val exchangeRateEntities = saveExchangeRates(currencyEntityIdsAndExchangeRates,
													 exchangeRatesResponseEntity.id)

        return exchangeRatesResponseEntity to exchangeRateEntities
    }

    private fun saveNewCurrencies(exchangeRateResponse: ExchangeRateResponse) {
        val recordedCurrencySymbols = currencyDao.getAll().map { currencyEntities ->
            currencyEntities.symbol
        }

        exchangeRateResponse.exchangeRates.map { exchangeRate ->
            exchangeRate.currency
        }.plus(exchangeRateResponse.baseCurrency).filter { currency ->
            currency.symbol !in recordedCurrencySymbols
        }.distinctBy { currency ->
            currency.symbol
        }.forEach { currency ->
            currencyDao.save(currency.symbol)
        }
    }

    private fun saveExchangeRates(currencyEntityIdsAndRates: Map<Int, Double>, exchangeRateRequestId: Int): List<ExchangeRateEntity> {
        return currencyEntityIdsAndRates.map { (currencyEntityId, rate) ->
            exchangeRateDao.saveRate(currencyEntityId, rate, exchangeRateRequestId)
        }
    }
}
