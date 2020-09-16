package no.esa.aop.annotation

import no.esa.aop.aspect.FailureRate

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Fails(val failureRate: FailureRate = FailureRate.SOMETIMES)
