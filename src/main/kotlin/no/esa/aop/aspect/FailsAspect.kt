package no.esa.aop.aspect

import no.esa.aop.annotation.Fails
import no.esa.aop.utils.getAnnotation
import no.esa.aop.utils.getKClass
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import kotlin.random.Random
import kotlin.reflect.KClass

@Aspect
@Component
class FailsAspect {

	companion object {
		private val DEFAULT_FAILURE_RATE = FailureRate.SOMETIMES
	}

	@Before("@annotation(no.esa.aop.annotation.Fails)")
	fun fail(joinPoint: JoinPoint) {
		val kClass = getKClass(joinPoint)
		val functionName = joinPoint.signature.name

		if (kClass == null) {
			return
		} else {
			val failureRate = getAnnotation<Fails>(kClass, functionName)?.failureRate ?: DEFAULT_FAILURE_RATE

			val randomFailure = Random.nextDouble(0.0, 1.0) in 0.0..failureRate.percentage

			if (randomFailure) {
				val args = joinPoint.args.toList()
				val errorMessage = "${kClass.simpleName}.$functionName($args) failed " +
						"because it was annotated to $failureRate fail!"

				throw InducedFailsException(errorMessage)
			}
		}
	}

	private fun getFailureRateFromAnnotationParameterByKClassAndFunctionName(kClass: KClass<*>,
																			 functionName: String): FailureRate {
		return getAnnotation<Fails>(kClass, functionName)?.failureRate ?: DEFAULT_FAILURE_RATE
	}
}

class InducedFailsException(message: String) : RuntimeException(message)

/**
 * Used with [no.esa.aop.annotation.Fails] to simulate
 * exceptions occurring randomly in the code.
 */
enum class FailureRate(val percentage: Double) {
	RARELY(0.015),
	SOMETIMES(0.15),
	OFTEN(0.4)
}
