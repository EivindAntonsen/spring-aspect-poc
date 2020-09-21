package no.esa.aop.repository.exchangeraterequest

import no.esa.aop.annotation.DataAccess
import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.repository.QueryFileReader
import no.esa.aop.repository.entity.ExchangeRateResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ExchangeRateResponseDao(private val jdbcTemplate: JdbcTemplate) : IExchangeRateResponseDao {

	companion object {
		const val SCHEMA = "ecb"
		const val TABLE_NAME = "exchange_rate_response"
		const val PRIMARY_KEY = "id"
		const val BASE_CURRENCY_ID = "base_currency_id"
		const val DATETIME = "datetime"
	}

	private val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

	@DataAccess
	@Logged(APIType.DATA_ACCESS)
	override fun save(dateTime: LocalDateTime, baseCurrencyEntityId: Int): ExchangeRateResponseEntity {
		val parameters = MapSqlParameterSource().apply {
			addValue(BASE_CURRENCY_ID, baseCurrencyEntityId)
			addValue(DATETIME, dateTime)
		}

		val id = SimpleJdbcInsert(jdbcTemplate).apply {
			schemaName = SCHEMA
			tableName = TABLE_NAME
			usingGeneratedKeyColumns(PRIMARY_KEY)
		}.executeAndReturnKey(parameters).toInt()

		return ExchangeRateResponseEntity(id, baseCurrencyEntityId, dateTime)
	}

	@DataAccess
	@Logged(APIType.DATA_ACCESS)
	override fun get(id: Int): ExchangeRateResponseEntity? {
		val query = QueryFileReader.readSqlFile(::get)
		val parameters = MapSqlParameterSource().apply {
			addValue(PRIMARY_KEY, id)
		}

		return namedTemplate.queryForObject(query, parameters) { rs, _ ->
			ExchangeRateResponseEntity(rs.getInt(PRIMARY_KEY),
									   rs.getInt(BASE_CURRENCY_ID),
									   rs.getTimestamp(DATETIME).toLocalDateTime())
		}
	}

	@DataAccess
	@Logged(APIType.DATA_ACCESS)
	override fun getAll(): List<ExchangeRateResponseEntity> {
		val query = QueryFileReader.readSqlFile(::getAll)

		return jdbcTemplate.query(query) { rs, _ ->
			ExchangeRateResponseEntity(rs.getInt(PRIMARY_KEY),
									   rs.getInt(BASE_CURRENCY_ID),
									   rs.getTimestamp(DATETIME).toLocalDateTime())
		}
	}
}
