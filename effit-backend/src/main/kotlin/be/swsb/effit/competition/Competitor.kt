package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.util.RestApiExposed
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Competitor(@Id val id: UUID = UUID.randomUUID(),
                      val name: String,
                      var totalScore: Int): RestApiExposed {

    @ManyToMany
    private var _completedChallenges: List<Challenge> = emptyList()

    val completedChallenges: List<Challenge>
        get() = _completedChallenges

    fun completeChallenge(challenge: Challenge) {
        _completedChallenges = _completedChallenges + challenge
    }

    fun awardPoints(points: Int) {
        totalScore += points
    }
}