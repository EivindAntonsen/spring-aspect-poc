package no.esa.aop.exception

class NoSuchCurrencyException(id: Int): RuntimeException("No currency found matching id $id!")
