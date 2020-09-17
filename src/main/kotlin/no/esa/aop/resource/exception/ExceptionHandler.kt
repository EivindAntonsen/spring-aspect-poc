package no.esa.aop.resource.exception

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*
import no.esa.aop.resource.model.Error as ModelError

@ControllerAdvice
class ExceptionHandler(private val resourceBundle: ResourceBundle,
                       private val logger: Logger) {

    @ExceptionHandler(ApiException.BadRequestException::class)
    fun handle(apiException: ApiException.BadRequestException): ResponseEntity<ModelError> {
        logger.error(apiException.message)

        val errorMessage = resourceBundle.getString("error.badRequest")
        val modelError = ModelError(errorMessage)

        return ResponseEntity.badRequest().body(modelError)
    }

    @ExceptionHandler(ApiException.NoPreviouslyRecordedResponseException::class)
    fun handle(apiException: ApiException.NoPreviouslyRecordedResponseException): ResponseEntity<ModelError> {
        logger.error(apiException.message)

        val errorMessage = resourceBundle.getString("error.noPreviouslyRecordedResponse")
        val modelError = ModelError(errorMessage)

        return ResponseEntity.badRequest().body(modelError)
    }

    @ExceptionHandler(ApiException.UnknownErrorException::class)
    fun handle(apiException: ApiException.UnknownErrorException): ResponseEntity<ModelError> {
        logger.error(apiException.message)

        val errorMessage = resourceBundle.getString("error.unknown")
        val modelError = ModelError(errorMessage)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(modelError)
    }

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<ModelError> {
        logger.error(exception.message)

        val errorMessage = resourceBundle.getString("error.unknown")
        val modelError = ModelError(errorMessage)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(modelError)
    }
}