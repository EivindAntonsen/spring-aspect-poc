package no.esa.aop.service.exchangerate

import no.esa.aop.integration.ecb.ExchangeRateRestInterface
import no.esa.aop.repository.currency.ICurrencyDao
import no.esa.aop.repository.entity.CurrencyEntity
import no.esa.aop.repository.entity.ExchangeRateEntity
import no.esa.aop.repository.entity.ExchangeRateRequestEntity
import no.esa.aop.repository.exchangerate.IExchangeRateDao
import no.esa.aop.repository.exchangeraterequest.IExchangeRateRequestDao
import no.esa.aop.service.domain.ExchangeRate
import no.esa.aop.service.domain.ExchangeRateRequest
import no.esa.aop.service.mapper.ExchangeRatesRequestMapper
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(private val exchangeRateRestInterface: ExchangeRateRestInterface,
						  private val currencyDao: ICurrencyDao,
						  private val exchangeRateDao: IExchangeRateDao,
						  private val exchangeRateRequestDao: IExchangeRateRequestDao) : IExchangeRateService {

	override fun getLatestRates(): ExchangeRateRequest {
		val ecbExchangeRatesRequest = exchangeRateRestInterface.requestExchangeRates()

		val exchangeRatesRequest = if (ecbExchangeRatesRequest.statusCode.is2xxSuccessful) {
			ExchangeRatesRequestMapper.ecbRequestToDomainRequest(ecbExchangeRatesRequest.body!!)
		} else throw RuntimeException("Test!")

		saveRequest(exchangeRatesRequest)

		return exchangeRatesRequest
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

	fun saveRequest(exchangeRateRequest: ExchangeRateRequest): Pair<ExchangeRateRequestEntity, List<ExchangeRateEntity>> {
		saveNewCurrencies(exchangeRateRequest)

		val currencyEntities = currencyDao.getAll()
		val baseCurrencyEntity = getBaseCurrencyEntityId(currencyEntities, exchangeRateRequest.baseCurrency.symbol)
		val exchangeRatesRequestEntity = exchangeRateRequestDao.save(exchangeRateRequest.dateTime, baseCurrencyEntity.id)
		val currencyEntityIdsAndExchangeRates = getCurrencyEntityIdsAndExchangeRates(currencyEntities,
																					 baseCurrencyEntity.id,
																					 exchangeRateRequest.exchangeRates)
		val exchangeRateEntities = saveExchangeRates(currencyEntityIdsAndExchangeRates,
													 exchangeRatesRequestEntity.id)

		return exchangeRatesRequestEntity to exchangeRateEntities
	}

	fun saveNewCurrencies(exchangeRateRequest: ExchangeRateRequest) {
		exchangeRateRequest.exchangeRates.map { exchangeRate ->
			exchangeRate.currency.symbol
		}.plus(exchangeRateRequest.baseCurrency.symbol).forEach {
			currencyDao.save(it)
		}
	}

	fun saveExchangeRates(currencyEntityIdsAndRates: Map<Int, Double>, exchangeRateRequestId: Int): List<ExchangeRateEntity> {
		return currencyEntityIdsAndRates.map { (currencyEntityId, rate) ->
			exchangeRateDao.saveRate(currencyEntityId, rate, exchangeRateRequestId)
		}
	}
}
