package be.swsb.effit.backend.domain.query.competition

import be.swsb.effit.backend.domain.command.competition.CompetitionRepository
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.backend.domain.query.QueryHandler
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindCompetition

class FindCompetitionQueryHandler(
    private val competitionRepository: CompetitionRepository
) : QueryHandler<Competition, FindCompetition> {
    override fun handle(query: FindCompetition): Competition {
        return competitionRepository.findByCompetitionIdentifier(query.id)
                ?: throw EntityNotFoundDomainRuntimeException("Competition with id ${query.id.id} not found")
    }

    override fun getQueryType(): Class<FindCompetition> {
        return FindCompetition::class.java
    }
}
