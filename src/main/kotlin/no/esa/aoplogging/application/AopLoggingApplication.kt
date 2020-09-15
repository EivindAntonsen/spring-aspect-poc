package no.esa.aoplogging.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication(scanBasePackages = ["no.esa.aoplogging"])
@ComponentScan(basePackages = ["no.esa.aoplogging"])
@EnableSwagger2
class AopLoggingApplication

fun main(args: Array<String>) {
	runApplication<AopLoggingApplication>(*args)
}
