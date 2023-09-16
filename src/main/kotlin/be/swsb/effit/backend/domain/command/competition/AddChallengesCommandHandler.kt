package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.domain.command.CommandHandler
import be.swsb.effit.backend.domain.command.competition.CompetitionCommands.AddChallenges
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindCompetition
import be.swsb.effit.backend.messaging.query.QueryExecutor

class AddChallengesCommandHandler(
    private val queryExecutor: QueryExecutor,
    private val competitionRepository: CompetitionRepository
) : CommandHandler<Competition, AddChallenges> {

    override fun handle(command: AddChallenges): Competition {
        val foundCompetition = findCompetition(command.id)
        command.challengesToAdd.forEach { foundCompetition.addChallenge(it) }
        return competitionRepository.save(foundCompetition)
    }

    private fun findCompetition(competitionId: CompetitionId): Competition {
        return queryExecutor.execute(FindCompetition(competitionId))
    }

    override fun getCommandType(): Class<AddChallenges> {
        return AddChallenges::class.java
    }
}
