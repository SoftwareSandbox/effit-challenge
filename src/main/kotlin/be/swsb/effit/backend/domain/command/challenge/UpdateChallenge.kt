package be.swsb.effit.backend.domain.command.challenge

import be.swsb.effit.backend.domain.command.CommandHandler
import be.swsb.effit.backend.domain.command.challenge.ChallengeCommands.UpdateChallenge
import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.core.challenge.ChallengeRepository
import be.swsb.effit.backend.domain.query.challenge.FindChallenge
import be.swsb.effit.backend.messaging.query.QueryExecutor

class UpdateChallengeCommandHandler(private val queryExecutor: QueryExecutor,
                                    private val challengeRepository: ChallengeRepository
)
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
