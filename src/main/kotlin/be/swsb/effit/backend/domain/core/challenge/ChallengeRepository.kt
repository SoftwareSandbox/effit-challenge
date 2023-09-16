package be.swsb.effit.backend.domain.core.challenge

import java.util.*

interface ChallengeRepository {
    fun findByIdOrNull(challengeId: UUID): Challenge?
    fun save(challenge: Challenge): Challenge
}