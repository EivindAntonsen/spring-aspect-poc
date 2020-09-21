package no.esa.aop.utils

sealed class Outcome<out T : Any> {
	data class Success<out T : Any>(val value: T) : Outcome<T>()
	data class Error(val message: String, val cause: Throwable? = null) : Outcome<Nothing>()
}
