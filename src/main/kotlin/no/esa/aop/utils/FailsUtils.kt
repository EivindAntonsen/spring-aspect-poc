package no.esa.aop.utils

import kotlin.random.Random

enum class FailureRate(val percentage: Double) {
    RARELY(0.015),
    SOMETIMES(0.15),
    OFTEN(0.4)
}

class RandomException(message: String) : RuntimeException(message)

/**
 * Used to randomly trigger exceptions based on the passed [failureRate].
 */
fun maybeFail(failureRate: FailureRate) {
    val randomFailure = Random.nextDouble(0.0, 1.0) in 0.0..failureRate.percentage

    if (randomFailure) {
        val errorMessage = "Random failure!"

        throw RandomException(errorMessage)
    }
}