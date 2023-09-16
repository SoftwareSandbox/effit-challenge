package be.swsb.effit.backend.domain.query.competition

import be.swsb.effit.backend.domain.command.competition.CompetitionRepository
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.messaging.query.QueryHandler
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.MaybeFindCompetition

class MaybeFindCompetitionQueryHandler(val competitionRepository: CompetitionRepository) :
    QueryHandler<Competition?, MaybeFindCompetition> {
    override fun handle(query: MaybeFindCompetition): Competition? {
        return competitionRepository.findByCompetitionIdentifier(query.id)
    }

    override fun getQueryType(): Class<MaybeFindCompetition> {
        return MaybeFindCompetition::class.java
    }
}
