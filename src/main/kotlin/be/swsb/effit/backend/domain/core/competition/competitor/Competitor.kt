package be.swsb.effit.backend.domain.core.competition.competitor

import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.core.exceptions.DomainValidationRuntimeException
import java.util.*

data class Competitor(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    private var _completedChallenges: List<Challenge> = emptyList()
) {

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
}
