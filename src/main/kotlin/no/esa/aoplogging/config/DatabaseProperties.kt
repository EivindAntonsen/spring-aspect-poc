package no.esa.aoplogging.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class DatabaseProperties(@Value("\${database.username}") val username: String,
							  @Value("\${database.password}") val password: String,
							  @Value("\${database.url}") val url: String)
