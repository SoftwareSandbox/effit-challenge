package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.stereotype.Component

@Component
class AddChallengesCommandHandler(
        private val queryExecutor: QueryExecutor,
        private val challengeRepository: ChallengeRepository,
        private val competitionRepository: CompetitionRepository
) : CommandHandler<Competition, AddChallenges> {

    override fun handle(command: AddChallenges): Competition {
        val foundCompetition = findCompetition(command.id)
        command.challengesToAdd.forEach {
            val newlyCreatedChallenge = createNewChallenge(it)
            val persistedChallenge = challengeRepository.save(newlyCreatedChallenge)
            foundCompetition.addChallenge(persistedChallenge)
        }
        return competitionRepository.save(foundCompetition)
    }

    private fun createNewChallenge(challengeToAdd: ChallengeToAdd): Challenge {
        return Challenge(
                name = challengeToAdd.name,
                points = challengeToAdd.points,
                description = challengeToAdd.description
        )
    }

    private fun findCompetition(competitionId: CompetitionId): Competition {
        return queryExecutor.execute(FindCompetition(competitionId))
    }

    override fun getCommandType(): Class<AddChallenges> {
        return AddChallenges::class.java
    }
}
