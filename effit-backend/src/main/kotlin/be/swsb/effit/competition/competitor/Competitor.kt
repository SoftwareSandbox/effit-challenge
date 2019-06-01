package be.swsb.effit.competition.competitor

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.util.RestApiExposed
import com.fasterxml.jackson.annotation.JsonSetter
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Competitor(@Id val id: UUID = UUID.randomUUID(),
                      val name: String,
                      @ManyToMany
                      @JsonSetter("completedChallenges")
                      private var _completedChallenges: List<Challenge> = emptyList()
                      ): RestApiExposed {

    val totalScore: Int
        get() = _completedChallenges.sumBy { it.points }

    val completedChallenges: List<Challenge>
        get() = _completedChallenges

    fun completeChallenge(challenge: Challenge) {
        _completedChallenges = _completedChallenges + challenge
    }

}