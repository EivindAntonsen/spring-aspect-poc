package no.esa.aoplogging.exception

class NoSuchCurrencyException(id: Int): RuntimeException("No currency found matching id $id!")
