package no.esa.aop.aspect.dataaccess

import no.esa.aop.repository.exception.DataAccessException
import no.esa.aop.utils.getKClass
import no.esa.aop.utils.getKFunction
import no.esa.aop.utils.getLogger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

@Aspect
@Component
class DataAccessAspect {

	@Around("@annotation(no.esa.aop.annotation.DataAccess)")
	fun dataAccessOperation(joinPoint: ProceedingJoinPoint): Any? {
		val kFunction = getKFunction(joinPoint)

		return try {
			joinPoint.proceed()
		} catch (error: Exception) {
			getLogger(joinPoint).error(error.message)

			if (kFunction != null) {
				throw DataAccessException(kFunction, error)
			} else throw error
		}
	}
}
