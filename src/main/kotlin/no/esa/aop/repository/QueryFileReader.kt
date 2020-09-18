package no.esa.aop.repository

import no.esa.aop.repository.exception.QueryFileNotFoundException
import org.slf4j.LoggerFactory
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

object QueryFileReader {

    private val logger = LoggerFactory.getLogger("no.get.aop.repository.QueryFileReader")

    fun readSqlFile(callingFunction: KFunction<*>): String {
        val className = callingFunction.javaMethod?.declaringClass?.simpleName?.toLowerCase()
        val path = "/db/queries/$className/${callingFunction.name}.sql"

        return try {
            this::class.java.getResource(path).readText()
        } catch (error: Exception) {
            val message = "Attempt to read from $path failed: ${error.message}."
            logger.error(message)

            throw QueryFileNotFoundException(message)
        }
    }
}