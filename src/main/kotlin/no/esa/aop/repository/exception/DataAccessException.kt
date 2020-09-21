package no.esa.aop.repository.exception

import kotlin.reflect.KFunction

data class DataAccessException(val callingFunction: KFunction<*>,
							   override val cause: Throwable?) : RuntimeException()
