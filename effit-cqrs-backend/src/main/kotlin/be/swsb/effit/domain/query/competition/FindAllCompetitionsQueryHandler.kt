package be.swsb.effit.domain.query.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.query.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindAllCompetitionsQueryHandler(private val competitionRepository: CompetitionRepository) : QueryHandler<List<Competition>, FindAllCompetitions> {

    override fun handle(query: FindAllCompetitions): List<Competition> {
        return competitionRepository.findAll()
    }

    override fun getQueryType(): Class<FindAllCompetitions> {
        return FindAllCompetitions::class.java
    }
}
