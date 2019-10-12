package be.swsb.effit.domain.query.challenge

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.query.QueryHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class FindChallengeQueryHandler(val challengeRepository: ChallengeRepository) : QueryHandler<Challenge?, FindChallenge> {
    override fun handle(query: FindChallenge): Challenge? {
        return challengeRepository.findByIdOrNull(query.challengeId)
    }

    override fun getQueryType(): Class<FindChallenge> {
        return FindChallenge::class.java
    }
}
