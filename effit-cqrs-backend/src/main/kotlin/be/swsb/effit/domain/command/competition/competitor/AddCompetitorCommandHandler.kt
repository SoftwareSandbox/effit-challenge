package be.swsb.effit.domain.command.competition.competitor

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.domain.command.competition.AddCompetitor
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.stereotype.Component

@Component
class AddCompetitorCommandHandler(private val queryExecutor: QueryExecutor,
                                  private val competitionRepository: CompetitionRepository)
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
