package no.esa.aoplogging.aspect

import no.esa.aoplogging.annotation.Logged
import no.esa.aoplogging.enums.APIType
import no.esa.aoplogging.utils.abbreviate
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.boot.logging.LogLevel.*
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.functions

@Aspect
@Component
class LogAspect {

	companion object {
		private val DEFAULT_LOG_LEVEL = WARN
		private val DEFAULT_API_TYPE = APIType.INTERNAL
		private const val MAX_LENGTH_API_TYPE_EVENT_NAME = 8
		private const val MAX_TYPE_LENGTH = 18
	}

	private val defaultLogger = LoggerFactory.getLogger(this::class.java)

	@Around("@annotation(no.esa.aoplogging.annotation.Logged)")
	fun log(joinPoint: ProceedingJoinPoint): Any? {
		val logger = getLogger(joinPoint)
		val (signature, args) = getSignatureAndArgs(joinPoint)
		val functionName = joinPoint.signature.name
		val kClass = getKClass(joinPoint)
		val (apiType, logLevel) = getApiTypeAndLogLevel(kClass, functionName)
		val firstEventName = padEventName(apiType.firstEventName)
		val secondEventName = padEventName(apiType.secondEventName)

		val firstEventMessage = "$firstEventName\t$signature$args"

		log(firstEventMessage, logLevel, logger)

		val (result, duration) = executeAndMeasureTimeMillis {
			joinPoint.proceed()
		}

		val secondEventMessage = "$secondEventName\t${getResultAsFormattedString(result)}\tin ${duration}ms."

		log(secondEventMessage, logLevel, logger)

		return result
	}

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

	private fun log(message: String,
					logLevel: LogLevel,
					logger: Logger) {

		when (logLevel) {
			DEBUG -> logger.debug(message)
			INFO -> logger.info(message)
			ERROR, FATAL -> logger.error(message)
			else -> logger.warn(message)
		}
	}

	private fun getKClass(joinPoint: ProceedingJoinPoint): KClass<out Any>? {
		// Removing type name from signature string
		val typeAndQualifiedFunctionName = joinPoint.signature.toString().split(" ")
		val qualifiedFunctionName = typeAndQualifiedFunctionName[1]

		// Reducing to a qualified class name
		val qualifiedClassAndFunctionName = qualifiedFunctionName.split(".${joinPoint.signature.name}")
		val qualifiedClassName = qualifiedClassAndFunctionName[0]

		return try {
			Class.forName(qualifiedClassName).kotlin
		} catch (error: ClassNotFoundException) {
			// This just means we have no specific log level and event name, so we'll use
			// default values for those two instead.
			null
		} catch (error: Exception) {
			// This is something else, so at least log it.
			defaultLogger.warn(error.message)
			null
		}
	}

	private fun getApiTypeAndLogLevel(kClass: KClass<*>?, functionName: String): Pair<APIType, LogLevel> {
		return kClass?.let {
			getLoggedAnnotationParameters(it, functionName)
		} ?: DEFAULT_API_TYPE to DEFAULT_LOG_LEVEL
	}

	private fun getLoggedAnnotationParameters(kClass: KClass<*>, functionName: String): Pair<APIType, LogLevel> {
		val annotation = kClass.functions.firstOrNull { function ->
			function.name == functionName
		}?.annotations?.filterIsInstance<Logged>()?.firstOrNull()

		val apiType = annotation?.apiType ?: DEFAULT_API_TYPE
		val logLevel = annotation?.logLevel ?: DEFAULT_LOG_LEVEL

		return apiType to logLevel
	}

	private fun getSignatureAndArgs(joinPoint: ProceedingJoinPoint): Pair<String, String> {
		val signature = getAbbreviatedSignature(joinPoint)
		val args = getArguments(joinPoint)

		return signature to args
	}

	private fun getLogger(joinPoint: ProceedingJoinPoint): Logger {
		return LoggerFactory.getLogger(joinPoint.signature.declaringTypeName)
	}

	private fun getArguments(joinPoint: ProceedingJoinPoint): String {
		return joinPoint.args.toList().toString().abbreviate()
	}

	private fun getAbbreviatedSignature(joinPoint: ProceedingJoinPoint): String {
		val dataTypeAndRemainingSignature = joinPoint.signature
				.toString()
				.replace("no.esa.aoplogging.", "n.e.a.")
				.replace(" ", "\t")
				.split("\t")
		val paddedDataTypePart = dataTypeAndRemainingSignature[0].padEnd(MAX_TYPE_LENGTH)
		val signatureWithoutGenericArguments = dataTypeAndRemainingSignature[1].split("(")[0]


		return "$paddedDataTypePart$signatureWithoutGenericArguments"
	}

	private fun getResultAsFormattedString(any: Any?): String {
		return any?.let { result ->
			val value = result.toString().abbreviate()

			StringBuffer().apply {
				getClassNameFromObject(result).let { className ->
					if (className != null) {
						append(className.padEnd(MAX_TYPE_LENGTH))
					} else append("Anonymous Object".padEnd(MAX_TYPE_LENGTH))
				}

				if (result is Collection<*>) {
					val type = getElementType(result)
					val size = result.size

					append("<$type>($size)")
				} else append(value)
			}.toString().abbreviate()
		} ?: "null".padEnd(MAX_TYPE_LENGTH)
	}

	private fun getClassNameFromObject(any: Any?): String? = any?.let { it::class.simpleName }

	private fun areAllElementsOfSameClass(collection: Collection<*>): Boolean {
		return collection.asSequence().filterNotNull().zipWithNext { current, next ->
			current::class.simpleName == next::class.simpleName
		}.all { it }
	}

	private fun getElementType(collection: Collection<*>): String? {
		return if (collection.isEmpty()) "<*>" else {
			val allNonNullsShareSimpleName = areAllElementsOfSameClass(collection)

			if (allNonNullsShareSimpleName) {
				val collectionWithoutNulls = collection.filterNotNull()

				if (collectionWithoutNulls.isEmpty()) {
					"<*>"
				} else collectionWithoutNulls.first()::class.simpleName ?: "null"
			} else "<R>"
		}
	}
}
