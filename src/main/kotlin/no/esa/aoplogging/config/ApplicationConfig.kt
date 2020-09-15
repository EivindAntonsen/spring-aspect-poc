package no.esa.aoplogging.config

import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@SpringBootConfiguration
@ComponentScan
@PropertySource("file:src/main/resources/env.properties")
class ApplicationConfig {

	@Bean("ecb")
	fun restTemplate(): RestTemplate {
		return RestTemplate(SimpleClientHttpRequestFactory()).apply {
			requestFactory = SimpleClientHttpRequestFactory().apply {
				setConnectTimeout(60000)
				setReadTimeout(60000)
			}
		}
	}
}
