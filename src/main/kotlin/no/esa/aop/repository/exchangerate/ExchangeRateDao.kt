package no.esa.aop.repository.exchangerate

import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.integration.ecb.domain.EcbExchangeRateRequest
import no.esa.aop.repository.entity.ExchangeRateEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class ExchangeRateDao(private val jdbcTemplate: JdbcTemplate): IExchangeRateDao {

	companion object {
		const val SCHEMA = "ecb"
		const val TABLE_NAME = "exchange_rate"
		const val PRIMARY_KEY = "id"
		const val EXCHANGE_RATE_REQUEST_ID = "exchange_rate_request_id"
		const val CURRENCY_ID = "currency_id"
		const val RATE = "rate"
	}

	private val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

	override fun getPrevious(): EcbExchangeRateRequest {
		TODO("Not yet implemented")
	}

	@Logged(APIType.DATA_ACCESS)
	override fun saveRate(currencyEntityId: Int, rate: Double, exchangeRateRequestId: Int): ExchangeRateEntity {
		val parameters = MapSqlParameterSource().apply {
			addValue(EXCHANGE_RATE_REQUEST_ID, exchangeRateRequestId)
			addValue(CURRENCY_ID, currencyEntityId)
			addValue(RATE, rate)
		}

		val id = SimpleJdbcInsert(jdbcTemplate).apply {
			schemaName = SCHEMA
			tableName = TABLE_NAME
			usingGeneratedKeyColumns(PRIMARY_KEY)
		}.executeAndReturnKey(parameters).toInt()

		return ExchangeRateEntity(id, currencyEntityId, rate)
	}

	override fun getRatesFor(currency: String): Double {
		TODO("Not yet implemented")
	}
}
