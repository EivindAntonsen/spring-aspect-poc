package no.esa.aop.repository

import no.esa.aop.repository.exception.QueryFileNotFoundException
import org.slf4j.LoggerFactory
import kotlin.reflect.KFunction

object QueryFileReader {

    private val logger = LoggerFactory.getLogger("no.get.aop.repository.QueryFileReader")

    fun readSqlFile(callingFunction: KFunction<*>): String {
        val className = callingFunction.javaClass.enclosingClass.simpleName.toLowerCase()
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