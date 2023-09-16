package be.swsb.effit.backend.domain.command.competition.competitor

import be.swsb.effit.backend.domain.command.CommandHandler
import be.swsb.effit.backend.domain.command.competition.CompetitionCommands.AddCompetitor
import be.swsb.effit.backend.domain.command.competition.CompetitionRepository
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindCompetition
import be.swsb.effit.backend.messaging.query.QueryExecutor

class AddCompetitorCommandHandler(private val queryExecutor: QueryExecutor,
                                  private val competitionRepository: CompetitionRepository
)
    : CommandHandler<Competition, AddCompetitor> {

    override fun handle(command: AddCompetitor): Competition {
        val competition = queryExecutor.execute(FindCompetition(command.id))
        competition.addCompetitor(command.competitorToAdd)
        return competitionRepository.save(competition)
    }

    override fun getCommandType(): Class<AddCompetitor> {
        return AddCompetitor::class.java
    }
}
