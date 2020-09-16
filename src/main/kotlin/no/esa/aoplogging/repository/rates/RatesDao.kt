package no.esa.aoplogging.repository.rates

import no.esa.aoplogging.integration.domain.ExchangeRates
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class RatesDao(private val jdbcTemplate: JdbcTemplate): IRatesDao {



	private val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

	override fun getPrevious(): ExchangeRates {
		TODO("Not yet implemented")
	}

	override fun saveRates(exchangeRates: ExchangeRates): Int {
		TODO("Not yet implemented")
	}

	override fun getRatesFor(currency: String): Double {
		TODO("Not yet implemented")
	}
}
