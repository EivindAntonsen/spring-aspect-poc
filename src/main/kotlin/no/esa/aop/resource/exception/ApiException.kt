package no.esa.aop.resource.exception

sealed class ApiException : RuntimeException() {
    class NoPreviouslyRecordedResponseException(override val message: String?) : ApiException()
    class UnknownErrorException(override val message: String?) : ApiException()
    class BadRequestException(override val message: String?) : ApiException()
}