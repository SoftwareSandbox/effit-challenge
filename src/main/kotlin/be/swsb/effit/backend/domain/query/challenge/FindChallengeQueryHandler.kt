package be.swsb.effit.backend.domain.query.challenge

import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.core.challenge.ChallengeRepository
import be.swsb.effit.backend.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.backend.messaging.query.QueryHandler

class FindChallengeQueryHandler(private val challengeRepository: ChallengeRepository) :
    QueryHandler<Challenge, FindChallenge> {
    override fun handle(query: FindChallenge): Challenge {
        return challengeRepository.findByIdOrNull(query.challengeId)
                ?: throw EntityNotFoundDomainRuntimeException("Challenge with id ${query.challengeId} not found")
    }

    override fun getQueryType(): Class<FindChallenge> {
        return FindChallenge::class.java
    }
}
