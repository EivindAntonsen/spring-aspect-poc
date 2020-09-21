package no.esa.aop.resource.model

import no.esa.aop.utils.ecbDateTimeFormatter
import java.time.LocalDate

class Date(value: String) {
	val value: LocalDate = LocalDate.parse(value, ecbDateTimeFormatter)

	init {
		require(LocalDate.parse(value, ecbDateTimeFormatter).isAfter(EARLIEST_ALLOWED_DATE)) {
			"Only dates after year 1999 are allowed!"
		}
	}

	companion object {
		private val EARLIEST_ALLOWED_DATE = LocalDate.of(2000, 1, 1)
	}
}
