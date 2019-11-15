package be.swsb.effit.domain.command.competition.competitor

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.domain.command.competition.RemoveCompetitor
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.stereotype.Component

@Component
class RemoveCompetitorCommandHandler(private val queryExecutor: QueryExecutor,
                                     private val competitionRepository: CompetitionRepository)
    : CommandHandler<Competition, RemoveCompetitor> {

    override fun handle(command: RemoveCompetitor): Competition {
        val foundCompetition = queryExecutor.execute(FindCompetition(command.id))
        foundCompetition.removeCompetitor(command.competitorIdToRemove.id)
        return competitionRepository.save(foundCompetition)
    }

    override fun getCommandType(): Class<RemoveCompetitor> {
        return RemoveCompetitor::class.java
    }
}
