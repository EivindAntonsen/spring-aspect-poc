package no.esa.aop.aspect.logged

import no.esa.aop.annotation.Logged
import no.esa.aop.enums.APIType
import no.esa.aop.utils.*
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.springframework.boot.logging.LogLevel
import org.springframework.boot.logging.LogLevel.*
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Aspect
@Component
class LogAspect {

	companion object {
		private val DEFAULT_LOG_LEVEL = WARN
		private val DEFAULT_API_TYPE = APIType.INTERNAL
		private const val MAX_LENGTH_API_TYPE_EVENT_NAME = 8
	}

	@Around("@annotation(no.esa.aop.annotation.Logged)")
	fun log(joinPoint: ProceedingJoinPoint): Any? {
		val logger = getLogger(joinPoint)
		val args = getArguments(joinPoint)
		val functionName = joinPoint.signature.name
		val kClass = getKClass(joinPoint)
		val (apiType, logLevel) = getApiTypeAndLogLevel(kClass, functionName)

		val event = LoggableEvent(kClass,
								  functionName,
								  args,
								  LoggingInfo(apiType, logLevel, logger)) {
			joinPoint.proceed()
		}

		return executeAndLogEvent(event)
	}

	private fun <R> executeAndLogEvent(event: LoggableEvent<R>): R {
		val firstEventName = padEventName(event.loggingInfo.apiType.firstEventName)
		val firstEventMessage = "$firstEventName\t${event.functionName}\t${event.args.toString().abbreviate()}"

		log(firstEventMessage,
			event.loggingInfo.logLevel,
			event.loggingInfo.logger)

		val (result, duration) = executeAndMeasureTimeMillis {
			event.interceptedFunction()
		}

		val secondEventName = padEventName(event.loggingInfo.apiType.secondEventName)
		val secondEventMessage = "$secondEventName\t${result.toFormattedString()}\tin ${duration}ms."
		// todo - add custom log level object with severity to allow for 'less than' comparisons in loglvl
		val secondEventLogLevel = if (result is Outcome.Error) WARN else event.loggingInfo.logLevel

		log(secondEventMessage,
			secondEventLogLevel,
			event.loggingInfo.logger)

		return result
	}

	/**
	 * Finds the longest event name of all event names,
	 * and pads [apiEventName] to the same length by
	 * appending whitespaces to the end.
	 *
	 * @param apiEventName is the event name to be padded with whitespaces.
	 */
	private fun padEventName(apiEventName: String): String {
		val maxLength = APIType.values().toList().flatMap { apiType ->
			listOf(apiType.firstEventName.length,
				   apiType.secondEventName.length)
		}.distinct().maxOrNull() ?: MAX_LENGTH_API_TYPE_EVENT_NAME

		return apiEventName.padEnd(maxLength, ' ')
	}

	private fun <R> executeAndMeasureTimeMillis(f: () -> R): Pair<R, Long> {
		val start = System.currentTimeMillis()
		val result = f()

		return result to (System.currentTimeMillis() - start)
	}

	private fun log(message: String, logLevel: LogLevel, logger: Logger) {

		when (logLevel) {
			DEBUG -> logger.debug(message)
			INFO -> logger.info(message)
			ERROR, FATAL -> logger.error(message)
			else -> logger.warn(message)
		}
	}

	private fun getApiTypeAndLogLevel(kClass: KClass<*>?, functionName: String): Pair<APIType, LogLevel> {
		return if (kClass != null) {
			val annotation = getAnnotation<Logged>(kClass, functionName)

			val apiType = annotation?.apiType ?: DEFAULT_API_TYPE
			val logLevel = annotation?.logLevel ?: DEFAULT_LOG_LEVEL

			apiType to logLevel
		} else DEFAULT_API_TYPE to DEFAULT_LOG_LEVEL
	}

	private fun getArguments(joinPoint: ProceedingJoinPoint): List<Any> {
		return joinPoint.args.toList()
	}

	private fun Any?.toFormattedString(): String {
		return this?.let { result ->
			val className = getClassNameFromObject(result) ?: "Anonymous Object"

			StringBuffer().apply {
				when (result) {
					is Collection<*> -> append("$className(${result.size})\t$result")
					is Map<*, *> -> append("$className(${result.size})\t$result")
					else -> append("$className\t$result")
				}
			}.toString().abbreviate()
		} ?: "null"
	}

	private fun getClassNameFromObject(any: Any?): String? = any?.let { it::class.simpleName }
}
