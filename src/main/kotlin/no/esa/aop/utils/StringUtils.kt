package no.esa.aop.utils

private const val STRING_MAX_LENGTH = 100

fun String.abbreviate(maxLength: Int = STRING_MAX_LENGTH): String {
	return if (length > maxLength) {
		substring(0, maxLength - 3).plus("...")
	} else this
}

fun Any?.toStringSortedAlphabeticallyWithIdFirst() {
	if (this != null) {
		// Javas default Object.toString() impl
		// this::class.java.name + "@" + Integer.toHexString(hashCode())
	}
}
