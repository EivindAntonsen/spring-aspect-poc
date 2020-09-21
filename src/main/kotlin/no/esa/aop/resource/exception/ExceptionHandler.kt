package no.esa.aop.resource.exception

import no.esa.aop.repository.exception.DataAccessException
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.*
import kotlin.reflect.jvm.javaMethod
import no.esa.aop.resource.model.Error as ModelError

@ControllerAdvice
class ExceptionHandler(private val resourceBundle: ResourceBundle,
					   private val logger: Logger) {

	@ExceptionHandler(DataAccessException::class)
	fun handle(daoException: DataAccessException): ResponseEntity<ModelError> {
		val className = daoException.callingFunction.javaMethod?.declaringClass?.simpleName?.toLowerCase()

		if (className == null) {
			logger.warn("Handling data access exception as common exception!")
			handle(daoException as Exception)
		}

		val functionName = daoException.callingFunction.name
		val errorMessage = resourceBundle.getString("$className.$functionName")
		val modelError = ModelError(errorMessage)

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(modelError)
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException::class)
	fun handle(exception: MethodArgumentTypeMismatchException): ResponseEntity<ModelError> {
		logger.error(exception.message)

		val errorMessage = resourceBundle.getString("error.badInput")
		val modelError = ModelError(errorMessage)

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(modelError)
	}

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handle(exception: MethodArgumentNotValidException): ResponseEntity<ModelError> {
		logger.error(exception.message)

		val errorMessage = resourceBundle.getString("error.badInput")
		val modelError = ModelError(errorMessage)

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(modelError)
	}

	@ExceptionHandler(Exception::class)
	fun handle(exception: Exception): ResponseEntity<ModelError> {
		logger.error(exception.message)

		val errorMessage = resourceBundle.getString("error.unknown")
		val modelError = ModelError(errorMessage)

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(modelError)
	}
}
