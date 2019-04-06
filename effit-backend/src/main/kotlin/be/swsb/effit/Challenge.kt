package be.swsb.effit

import java.lang.IllegalStateException

data class Challenge(private val name: String,
                     private val points: Int,
                     private val description: String) {
    init {
        if (points < 0) throw IllegalStateException("Cannot create a Challenge with negative points")
        if (points == 0) throw IllegalStateException("Cannot create a Challenge with 0 points")
    }
}
