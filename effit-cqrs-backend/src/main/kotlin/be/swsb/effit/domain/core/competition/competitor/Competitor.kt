package be.swsb.effit.domain.core.competition.competitor

import be.swsb.effit.adapter.ui.util.RestApiExposed
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import com.fasterxml.jackson.annotation.JsonSetter
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Competitor(@Id val id: UUID = UUID.randomUUID(),
                      val name: String,
                      @ManyToMany(targetEntity = Challenge::class)
                      @JsonSetter("completedChallenges")
                      private var _completedChallenges: List<Challenge> = emptyList()
                      ): RestApiExposed {

    val totalScore: Int
        get() = _completedChallenges.sumBy { it.points }

    val completedChallenges: List<Challenge>
        get() = _completedChallenges

    init {
        if (name.length > 50) {
            throw DomainValidationRuntimeException("A Competitor name must be less than 50 characters.")
        }
    }

    fun completeChallenge(challenge: Challenge) {
        _completedChallenges.find { it == challenge }
                ?.let { throw ChallengeAlreadyCompletedDomainException(challenge.name) }

        _completedChallenges = _completedChallenges + challenge
    }

    companion object

}
