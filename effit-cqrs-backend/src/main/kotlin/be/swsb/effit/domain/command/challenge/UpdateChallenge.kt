package be.swsb.effit.domain.command.challenge

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.query.challenge.FindChallenge
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.stereotype.Component

@Component
class UpdateChallengeCommandHandler(private val queryExecutor: QueryExecutor,
                                    private val challengeRepository: ChallengeRepository)
    : CommandHandler<Challenge, UpdateChallenge> {
    override fun handle(command: UpdateChallenge): Challenge {
        val foundChallenge = queryExecutor.execute(FindChallenge(command.id))
        return challengeRepository.save(
                foundChallenge.copy(name = command.name,
                        points = command.points,
                        description = command.description)
        )
    }

    override fun getCommandType(): Class<UpdateChallenge> {
        return UpdateChallenge::class.java
    }
}
