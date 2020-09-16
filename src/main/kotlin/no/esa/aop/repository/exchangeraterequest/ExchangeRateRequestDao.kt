package no.esa.aop.repository.exchangeraterequest

import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.repository.entity.ExchangeRateRequestEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ExchangeRateRequestDao(private val jdbcTemplate: JdbcTemplate): IExchangeRateRequestDao {

	companion object {
		const val SCHEMA = "ecb"
		const val TABLE_NAME = "exchange_rate_request"
		const val PRIMARY_KEY = "id"
		const val BASE_CURRENCY_ID = "base_currency_id"
		const val DATETIME = "datetime"
	}

	private val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

	@Logged(APIType.DATA_ACCESS)
	override fun save(dateTime: LocalDateTime, baseCurrencyEntityId: Int): ExchangeRateRequestEntity {
		val parameters = MapSqlParameterSource().apply {
			addValue(BASE_CURRENCY_ID, baseCurrencyEntityId)
			addValue(DATETIME, dateTime)
		}

		val id = SimpleJdbcInsert(jdbcTemplate).apply {
			schemaName = SCHEMA
			tableName = TABLE_NAME
			usingGeneratedKeyColumns(PRIMARY_KEY)
		}.executeAndReturnKey(parameters).toInt()

		return ExchangeRateRequestEntity(id, baseCurrencyEntityId, dateTime)
	}

	override fun get(id: Int): ExchangeRateRequestEntity {
		TODO("Not yet implemented")
	}
}
