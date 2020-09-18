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

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<ModelError> {
        logger.error(exception.message)

        val errorMessage = resourceBundle.getString("error.unknown")
        val modelError = ModelError(errorMessage)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(modelError)
    }
}
