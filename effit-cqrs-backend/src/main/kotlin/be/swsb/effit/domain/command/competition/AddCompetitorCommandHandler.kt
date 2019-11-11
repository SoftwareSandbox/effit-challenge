package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.adapter.sql.competition.competitor.CompetitorRepository
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.core.competition.competitor.CompetitorName
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.stereotype.Component

@Component
class AddCompetitorCommandHandler(private val queryExecutor: QueryExecutor,
                                  private val competitionRepository: CompetitionRepository,
                                  private val competitorRepository: CompetitorRepository)
    : CommandHandler<Competition, AddCompetitor> {

    override fun handle(command: AddCompetitor): Competition {
        val competition = queryExecutor.execute(FindCompetition(command.id))
        val savedCompetitor = createAndSaveCompetitor(command.competitorToAdd)
        competition.addCompetitor(savedCompetitor)
        return competitionRepository.save(competition)
    }

    private fun createAndSaveCompetitor(competitorToAdd: CompetitorName): Competitor {
        val createdCompetitor = Competitor(name = competitorToAdd.name)
        return competitorRepository.save(createdCompetitor)
    }

    override fun getCommandType(): Class<AddCompetitor> {
        return AddCompetitor::class.java
    }
}
