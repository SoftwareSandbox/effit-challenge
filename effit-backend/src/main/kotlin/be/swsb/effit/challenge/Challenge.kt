package be.swsb.effit.challenge

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Challenge(@Id val id: UUID = UUID.randomUUID(),
                     val name: String,
                     val points: Int,
                     val description: String) {

    init {
        if (points < 0) throw IllegalStateException("Cannot create a Challenge with negative points")
        if (points == 0) throw IllegalStateException("Cannot create a Challenge with 0 points")
    }
}
