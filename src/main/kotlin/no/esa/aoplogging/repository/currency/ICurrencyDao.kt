package no.esa.aoplogging.repository.currency

interface ICurrencyDao {
	fun save(symbol: String): Int
	fun get(id: Int): String
}
