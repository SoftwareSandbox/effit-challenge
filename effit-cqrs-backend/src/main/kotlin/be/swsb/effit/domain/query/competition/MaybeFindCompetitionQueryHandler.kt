package be.swsb.effit.domain.query.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.query.QueryHandler
import org.springframework.stereotype.Component

@Component
class MaybeFindCompetitionQueryHandler(val competitionRepository: CompetitionRepository) : QueryHandler<Competition?, MaybeFindCompetition> {
    override fun handle(query: MaybeFindCompetition): Competition? {
        return competitionRepository.findByCompetitionIdentifier(query.id)
    }

    override fun getQueryType(): Class<MaybeFindCompetition> {
        return MaybeFindCompetition::class.java
    }
}
