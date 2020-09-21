package no.esa.aop.enums

/**
 * Used for as prefix in log messages to indicate the origin and destination of a logged event.
 *
 * @property INTERNAL is for api calls contained within this module.
 * @property EXTERNAL is for api calls that either originate from outside or have an outbound destination.
 * @property DATA_ACCESS is for data access operations.
 */
enum class APIType(val firstEventName: String, val secondEventName: String) {
	INTERNAL("Call", "Result"),
	EXTERNAL("Request", "Response"),
	DATA_ACCESS("Query", "Result");
}
