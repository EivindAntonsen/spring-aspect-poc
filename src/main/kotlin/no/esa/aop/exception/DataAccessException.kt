package no.esa.aop.exception

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

data class DataAccessException(val callingClass: KClass<*>,
							   val callingFunction: KFunction<*>,
							   override val cause: Throwable?) : RuntimeException()
