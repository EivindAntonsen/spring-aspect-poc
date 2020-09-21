package no.esa.aop.repository.currency

import no.esa.aop.repository.entity.CurrencyEntity

interface ICurrencyDao {
	fun save(symbol: String): CurrencyEntity
	fun get(id: Int): String?
	fun getCurrencyEntitiesForSymbols(symbols: List<String>): List<CurrencyEntity>
	fun getAll(): List<CurrencyEntity>
}
