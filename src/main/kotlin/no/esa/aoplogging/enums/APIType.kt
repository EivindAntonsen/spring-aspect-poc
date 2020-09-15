package no.esa.aoplogging.enums

/**
 * Used for as prefix in log messages to indicate the origin and destination of a logged event.
 *
 * @property INTERNAL are for api calls contained within this module.
 * @property EXTERNAL are for api calls from this module to an external destination.
 * @property DATABASE are for data access operations.
 */
enum class APIType(val firstEventName: String, val secondEventName: String) {
	INTERNAL("Call", "Result"),
	EXTERNAL("Request", "Response"),
	DATABASE("Query", "Result");

	companion object {
		const val DEFAULT_FIRST_EVENT_NAME = "Function"
		const val DEFAULT_SECOND_EVENT_NAME = "Result"
	}
}
