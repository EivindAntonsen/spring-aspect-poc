package no.esa.aop.utils

import kotlin.random.Random

fun maybeFail(failureRate: FailureRate) {
    val randomDouble = Random.nextDouble(0.0, 1.0)

    if (randomDouble in 0.0..failureRate.percentage) {
        throw RuntimeException("Random exception!")
    }
}

/**
 * This file is used to induce random exceptions in order to see how they are handled.
 */

enum class FailureRate(val percentage: Double) {
    RARELY(0.003),
    SOMETIMES(0.085),
    OFTEN(0.15)
}