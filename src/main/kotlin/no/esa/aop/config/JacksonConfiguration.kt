package no.esa.aop.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {

	@Bean
	fun objectMapper(): ObjectMapper {
		return ObjectMapper().apply {
			configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
			configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
			configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
		}
	}
}
