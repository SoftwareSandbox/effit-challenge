package be.swsb.effit.domain.query.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.query.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindByCompetitionIdQueryHandler(val competitionRepository: CompetitionRepository) : QueryHandler<Competition?, FindByCompetitionId> {
    override fun handle(query: FindByCompetitionId): Competition? {
        return competitionRepository.findByCompetitionIdentifier(query.id)
    }

    override fun getQueryType(): Class<FindByCompetitionId> {
        return FindByCompetitionId::class.java
    }
}
