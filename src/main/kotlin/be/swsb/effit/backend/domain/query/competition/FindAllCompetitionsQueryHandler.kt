package be.swsb.effit.backend.domain.query.competition

import be.swsb.effit.backend.domain.command.competition.CompetitionRepository
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.messaging.query.QueryHandler
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindAllCompetitions

class FindAllCompetitionsQueryHandler(
    private val competitionRepository: CompetitionRepository
) : QueryHandler<List<Competition>, FindAllCompetitions> {

    override fun handle(query: FindAllCompetitions): List<Competition> {
        return competitionRepository.findAll()
    }

    override fun getQueryType(): Class<FindAllCompetitions> {
        return FindAllCompetitions::class.java
    }
}
