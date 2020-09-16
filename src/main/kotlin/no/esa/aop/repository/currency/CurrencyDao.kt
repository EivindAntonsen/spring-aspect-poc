package no.esa.aop.repository.currency

import no.esa.aop.annotation.DataAccess
import no.esa.aop.annotation.Fails
import no.esa.aop.annotation.Logged
import no.esa.aop.aspect.FailureRate
import no.esa.aop.enums.APIType
import no.esa.aop.exception.NoSuchCurrencyException
import no.esa.aop.repository.entity.CurrencyEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class CurrencyDao(private val jdbcTemplate: JdbcTemplate) : ICurrencyDao {

	companion object {
		const val SCHEMA = "ecb"
		const val TABLE_NAME = "currency"
		const val PRIMARY_KEY = "id"
		const val SYMBOL = "symbol"
	}

	private val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

	// @Fails(FailureRate.SOMETIMES)
	@DataAccess
	@Logged(APIType.DATA_ACCESS)
	override fun save(symbol: String): CurrencyEntity {
		val parameters = MapSqlParameterSource().apply {
			addValue(SYMBOL, symbol)
		}

		val id = SimpleJdbcInsert(jdbcTemplate).apply {
			schemaName = SCHEMA
			tableName = TABLE_NAME
			usingGeneratedKeyColumns(PRIMARY_KEY)
		}.executeAndReturnKey(parameters).toInt()

		return CurrencyEntity(id, symbol)
	}

	@DataAccess
	@Logged(APIType.DATA_ACCESS)
	override fun get(id: Int): String {
		val query = "select * from $SCHEMA.$TABLE_NAME where $PRIMARY_KEY = :id"
		val parameters = MapSqlParameterSource().apply {
			addValue(PRIMARY_KEY, id)
		}

		return namedTemplate.queryForObject(query, parameters) { rs, _ ->
			rs.getString(SYMBOL)
		} ?: throw NoSuchCurrencyException(id)
	}

	@DataAccess
	@Logged(APIType.DATA_ACCESS)
	override fun getCurrencyEntitiesForSymbols(symbols: List<String>): List<CurrencyEntity> {
		val query = "select * from $SCHEMA.$TABLE_NAME where $SYMBOL like :symbol"

		return symbols.mapNotNull { symbol ->
			val parameters = MapSqlParameterSource().apply {
				addValue(SYMBOL, symbol)
			}

			namedTemplate.queryForObject(query, parameters) { rs, _ ->
				CurrencyEntity(rs.getInt(PRIMARY_KEY), rs.getString(SYMBOL))
			}
		}
	}

	@DataAccess
	@Logged(APIType.DATA_ACCESS)
	override fun getAll(): List<CurrencyEntity> {
		return jdbcTemplate.query("select * from $SCHEMA.$TABLE_NAME") { rs, _ ->
			CurrencyEntity(rs.getInt(PRIMARY_KEY), rs.getString(SYMBOL))
		}
	}
}
