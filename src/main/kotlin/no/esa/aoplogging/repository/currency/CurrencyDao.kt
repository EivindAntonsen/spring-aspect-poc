package no.esa.aoplogging.repository.currency

import no.esa.aoplogging.exception.NoSuchCurrencyException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class CurrencyDao(private val jdbcTemplate: JdbcTemplate) : ICurrencyDao {

	companion object {
		const val SCHEMA = "currencies"
		const val TABLE_NAME = "currency"
		const val PRIMARY_KEY = "id"
		const val SYMBOL = "symbol"
	}

	private val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

	override fun save(symbol: String): Int {
		val parameters = MapSqlParameterSource().apply {
			addValue(SYMBOL, symbol)
		}

		return SimpleJdbcInsert(jdbcTemplate).executeAndReturnKey(parameters).toInt()
	}

	override fun get(id: Int): String {
		val query = "select * from $SCHEMA.$TABLE_NAME where $PRIMARY_KEY = :id"
		val parameters = MapSqlParameterSource().apply {
			addValue(PRIMARY_KEY, id)
		}

		return namedTemplate.queryForObject(query, parameters) { rs, _ ->
			rs.getString(SYMBOL)
		} ?: throw NoSuchCurrencyException(id)
	}
}
