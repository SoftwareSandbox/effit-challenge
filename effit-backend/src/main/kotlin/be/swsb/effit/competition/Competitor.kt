package be.swsb.effit.competition

import be.swsb.effit.util.RestApiExposed
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Competitor(@Id val id: UUID = UUID.randomUUID(),
                      val name: String,
                      var totalScore: Int): RestApiExposed {

    fun awardPoints(points: Int) {
        totalScore += points
    }
}