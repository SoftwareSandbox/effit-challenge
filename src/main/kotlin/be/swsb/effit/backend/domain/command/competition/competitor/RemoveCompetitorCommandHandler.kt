package be.swsb.effit.backend.domain.command.competition.competitor

import be.swsb.effit.backend.domain.command.CommandHandler
import be.swsb.effit.backend.domain.command.competition.CompetitionRepository
import be.swsb.effit.backend.domain.command.competition.CompetitionCommands.RemoveCompetitor
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindCompetition
import be.swsb.effit.backend.messaging.query.QueryExecutor

class RemoveCompetitorCommandHandler(
    private val queryExecutor: QueryExecutor,
    private val competitionRepository: CompetitionRepository
) : CommandHandler<Competition, RemoveCompetitor> {

    override fun handle(command: RemoveCompetitor): Competition {
        val foundCompetition = queryExecutor.execute(FindCompetition(command.id))
        foundCompetition.removeCompetitor(command.competitorIdToRemove.id)
        return competitionRepository.save(foundCompetition)
    }

    override fun getCommandType(): Class<RemoveCompetitor> {
        return RemoveCompetitor::class.java
    }
}
