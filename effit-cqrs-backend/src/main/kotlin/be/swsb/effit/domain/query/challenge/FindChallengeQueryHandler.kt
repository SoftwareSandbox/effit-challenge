package be.swsb.effit.domain.query.challenge

import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.query.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindChallengeQueryHandler: QueryHandler<Challenge?, FindChallenge> {
    override fun handle(query: FindChallenge): Challenge? {
        return null
    }

    override fun getQueryType(): Class<FindChallenge> {
        return FindChallenge::class.java
    }
}
