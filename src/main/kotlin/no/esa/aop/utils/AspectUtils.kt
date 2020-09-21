package no.esa.aop.utils

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.functions

/**
 * Attempts to get a KClass instance from a [joinPoint].
 *
 * Hacks away at a [org.aspectj.lang.JoinPoint.getSignature] until a fully qualified
 * function name is all that remains. That is then used for getting a javaClass instance
 * which is converted to a KClass instance.
 */
fun getKClass(joinPoint: JoinPoint): KClass<out Any>? {
	// Removing type name from signature string
	val typeAndQualifiedFunctionName = joinPoint.signature.toString().split(" ")
	val qualifiedFunctionName = typeAndQualifiedFunctionName[1]

	// Reducing to a qualified class name
	val qualifiedClassAndFunctionName = qualifiedFunctionName.split(".${joinPoint.signature.name}")
	val qualifiedClassName = qualifiedClassAndFunctionName[0]

	return try {
		Class.forName(qualifiedClassName).kotlin
	} catch (error: Exception) {
		null
	}
}

inline fun <reified T : Annotation> getAnnotation(kClass: KClass<*>, functionName: String): T? {
	return kClass.functions.firstOrNull { function ->
		function.name == functionName
	}?.annotations?.filterIsInstance<T>()?.firstOrNull()
}

fun getLogger(joinPoint: ProceedingJoinPoint): Logger {
	return LoggerFactory.getLogger(joinPoint.signature.declaringTypeName)
}
