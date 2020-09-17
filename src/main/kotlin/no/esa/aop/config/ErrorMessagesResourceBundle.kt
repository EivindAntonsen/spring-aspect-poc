package no.esa.aop.config

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*

@Component
class ErrorMessagesResourceBundle {

    @Bean("errorMessages")
    fun resourceBundle(): ResourceBundle {
        return ResourceBundle.getBundle("messages", Locale.ENGLISH)
    }
}