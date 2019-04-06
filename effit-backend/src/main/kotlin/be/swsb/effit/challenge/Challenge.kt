package be.swsb.effit.challenge

import java.lang.IllegalStateException

data class Challenge(val name: String,
                     val points: Int,
                     val description: String) {
    init {
        if (points < 0) throw IllegalStateException("Cannot create a Challenge with negative points")
        if (points == 0) throw IllegalStateException("Cannot create a Challenge with 0 points")
    }
}
