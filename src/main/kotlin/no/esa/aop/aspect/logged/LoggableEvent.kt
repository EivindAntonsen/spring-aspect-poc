package no.esa.aop.aspect.logged

import kotlin.reflect.KClass

/**
 * This is the actual event to be executed and logged.
 *
 * @param kClass is the enclosing class of the intercepted function.
 * @param functionName
 * @param args are the arguments passed to the intercepted function.
 * @param loggingInfo contains information about how to log the event
 * @param interceptedFunction is the intercepted function that will be executed.
 */
data class LoggableEvent<R>(val kClass: KClass<*>?,
							val functionName: String,
							val args: List<Any>,
							val loggingInfo: LoggingInfo,
							val interceptedFunction: () -> R)
