package no.esa.aop.repository.exchangerate

import no.esa.aop.annotation.DataAccess
import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.repository.QueryFileReader
import no.esa.aop.repository.entity.ExchangeRateEntity
import no.esa.aop.utils.FailureRate
import no.esa.aop.utils.maybeFail
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class ExchangeRateDao(private val jdbcTemplate: JdbcTemplate) : IExchangeRateDao {

    companion object {
        const val SCHEMA = "ecb"
        const val TABLE_NAME = "exchange_rate"
        const val PRIMARY_KEY = "id"
        const val EXCHANGE_RATE_RESPONSE_ID = "exchange_rate_response_id"
        const val CURRENCY_ID = "currency_id"
        const val RATE = "rate"
    }

    private val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    @DataAccess
    @Logged(APIType.DATA_ACCESS)
    override fun saveRate(currencyEntityId: Int, rate: Double, exchangeRateRequestId: Int): ExchangeRateEntity {
        maybeFail(FailureRate.RARELY)

        val parameters = MapSqlParameterSource().apply {
            addValue(EXCHANGE_RATE_RESPONSE_ID, exchangeRateRequestId)
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

    @DataAccess
    @Logged(APIType.DATA_ACCESS)
    override fun getByExchangeRateResponseId(id: Int): List<ExchangeRateEntity> {
        maybeFail(FailureRate.SOMETIMES)
        val query = QueryFileReader.readSqlFile(::getByExchangeRateResponseId)
        val parameters = MapSqlParameterSource().apply {
            addValue(EXCHANGE_RATE_RESPONSE_ID, id)
        }

        return namedTemplate.query(query, parameters) { rs, _ ->
            ExchangeRateEntity(rs.getInt(PRIMARY_KEY),
                               rs.getInt(CURRENCY_ID),
                               rs.getDouble(RATE))
        }
    }
}
