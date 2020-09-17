package no.esa.aop.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.Scope
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource

@SpringBootConfiguration
@ComponentScan
@PropertySource("file:src/main/resources/env.properties")
class ApplicationConfig(private val databaseProperties: DatabaseProperties) {

    @Bean
    @Scope("prototype")
    fun produceLogger(injectionPoint: InjectionPoint): Logger {
        val classOnWired = injectionPoint.member.declaringClass

        return LoggerFactory.getLogger(classOnWired)
    }

    @Bean("ecb")
    fun restTemplate(): RestTemplate {
        return RestTemplate(SimpleClientHttpRequestFactory()).apply {
            requestFactory = SimpleClientHttpRequestFactory().apply {
                setConnectTimeout(60000)
                setReadTimeout(60000)
            }
        }
    }

    @Bean
    fun dataSource(): DataSource {
        return HikariDataSource(HikariConfig().apply {
            username = databaseProperties.username
            password = databaseProperties.password
            jdbcUrl = databaseProperties.url
        })
    }

    @Bean
    fun flyway(): Flyway {
        val flyway = Flyway.configure()
                .dataSource(dataSource())
                .schemas("currencies")
                .locations("classpath:db/migration/common")
                .outOfOrder(true)
                .table("schema_version")
                .load()

        flyway.migrate()

        return flyway
    }
}
